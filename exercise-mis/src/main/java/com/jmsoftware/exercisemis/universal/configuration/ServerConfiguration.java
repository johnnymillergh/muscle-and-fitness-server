package com.jmsoftware.exercisemis.universal.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

/**
 * <h1>ServerConfiguration</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-04-26 16:02
 **/
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class ServerConfiguration implements ApplicationListener<WebServerInitializedEvent> {
    public static final String DEVELOPMENT_ENVIRONMENT_ALIAS = "dev";
    private final ProjectProperty projectProperty;
    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    /**
     * <p>Get base URL of backend server.</p>
     * <p>The result will be like:</p>
     * <ol>
     * <li>http://[serverIp]:[serverPort]/[contextPath]</li>
     * <li>https://[serverIp]/[contextPath]</li>
     * </ol>
     *
     * @return base URL
     * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
     * @date 2019-05-03 16:05
     */
    public String getBaseUrl() {
        return "http://" + this.getPublicIp() + ":" + serverPort + projectProperty.getContextPath();
    }

    /**
     * Find public IP address.
     *
     * @return public IP
     */
    public String getPublicIp() {
        if (projectProperty.getEnvironmentAlias().contains(DEVELOPMENT_ENVIRONMENT_ALIAS)) {
            return this.getInternetIp();
        }
        try {
            // An API provided by https://whatismyipaddress.com/api
            val url = new URL("https://ipv4bot.whatismyipaddress.com/");
            val sc = new BufferedReader(new InputStreamReader(url.openStream()));
            // Read system IP Address
            return sc.readLine().trim();
        } catch (Exception e) {
            log.error("Cannot execute properly to get IP address from https://whatismyipaddress.com/api", e);
        }
        return this.getInternetIp();
    }

    /**
     * Get internet IP.
     *
     * @return internet IP
     */
    private String getInternetIp() {
        val intranetIp = this.getIntranetIp();
        try {
            val networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            Enumeration<InetAddress> addresses;
            while (networks.hasMoreElements()) {
                addresses = networks.nextElement().getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip instanceof Inet4Address
                        && ip.isSiteLocalAddress()
                        && !ip.getHostAddress().equals(intranetIp)) {
                        return ip.getHostAddress();
                    }
                }
            }
            return intranetIp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get intranet IP.
     *
     * @return intranet IP
     */
    private String getIntranetIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

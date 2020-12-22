package com.jmsoftware.maf.muscleandfitnessserverspringbootstarter.helper;

import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
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
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-04-26 16:02
 **/
@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class IpHelper implements ApplicationListener<WebServerInitializedEvent> {
    private static final String DEVELOPMENT_ENVIRONMENT = "development";
    @Getter(AccessLevel.NONE)
    private final Environment environment;
    private int serverPort;

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    /**
     * Find public IP address.
     *
     * @return public IP
     */
    public String getPublicIp() {
        String jointProfiles = String.join(",", environment.getActiveProfiles());
        if (StrUtil.isNotBlank(jointProfiles)) {
            if (jointProfiles.contains(DEVELOPMENT_ENVIRONMENT)) {
                log.debug("Current active profiles for environment contains: {}", DEVELOPMENT_ENVIRONMENT);
                return this.getInternetIp();
            }
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

package com.jmsoftware.maf.springcloudstarter.helper;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.*;
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
        val jointProfiles = String.join(",", this.environment.getActiveProfiles());
        if (StrUtil.containsIgnoreCase(jointProfiles, DEVELOPMENT_ENVIRONMENT)) {
            log.debug("Current active profiles for environment contains: {}", DEVELOPMENT_ENVIRONMENT);
            return this.getInternetIp();
        }
        // An API provided by https://whatismyipaddress.com/api
        return HttpUtil.get("https://ipv4bot.whatismyipaddress.com/").trim();
    }

    /**
     * Get internet IP.
     *
     * @return internet IP
     */
    @SneakyThrows({SocketException.class})
    private String getInternetIp() {
        val intranetIp = this.getIntranetIp();
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
    }

    /**
     * Get intranet IP.
     *
     * @return intranet IP
     */
    @SneakyThrows({UnknownHostException.class})
    private String getIntranetIp() {
        return InetAddress.getLocalHost().getHostAddress();
    }
}

package com.jmsoftware.maf.springcloudstarter.helper

import cn.hutool.core.util.StrUtil
import cn.hutool.http.HttpUtil
import com.jmsoftware.maf.common.util.logger
import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.Environment
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 * <h1>ServerConfiguration</h1>
 *
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-04-26 16:02
 */
class IpHelper(
    private val environment: Environment
) : ApplicationListener<WebServerInitializedEvent> {
    companion object {
        private const val DEVELOPMENT_ENVIRONMENT = "development"
        private val log = logger()
    }

    var serverPort = 0

    override fun onApplicationEvent(event: WebServerInitializedEvent) {
        serverPort = event.webServer.port
    }

    /**
     * Find public IP address.
     *
     * @return public IP
     */
    fun getPublicIp(): String {
        val jointProfiles = java.lang.String.join(",", *environment.activeProfiles)
        if (StrUtil.containsIgnoreCase(jointProfiles, DEVELOPMENT_ENVIRONMENT)) {
            log.debug("Current active profiles for environment contains: {}", DEVELOPMENT_ENVIRONMENT)
            return internetIp()
        }
        // An API provided by https://whatismyipaddress.com/api
        return try {
            HttpUtil.get("https://ipv4bot.whatismyipaddress.com/").trim { it <= ' ' }
        } catch (e: Exception) {
            log.warn(
                "Failed to get public IP address, fallback to intranet IP address. Exception: {}",
                e.message
            )
            internetIp()
        }
    }

    /**
     * Get internet IP.
     *
     * @return internet IP
     */
    private fun internetIp(): String {
        val intranetIp = intranetIp()
        val networks = NetworkInterface.getNetworkInterfaces()
        var ip: InetAddress
        var addresses: Enumeration<InetAddress>
        while (networks.hasMoreElements()) {
            addresses = networks.nextElement().inetAddresses
            while (addresses.hasMoreElements()) {
                ip = addresses.nextElement()
                if (ip is Inet4Address
                    && ip.isSiteLocalAddress()
                    && ip.getHostAddress() != intranetIp
                ) {
                    return ip.getHostAddress()
                }
            }
        }
        return intranetIp
    }

    /**
     * Get intranet IP.
     *
     * @return intranet IP
     */
    private fun intranetIp(): String = InetAddress.getLocalHost().hostAddress
}

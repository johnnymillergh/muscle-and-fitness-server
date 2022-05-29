package com.jmsoftware.maf.reactivespringcloudstarter.helper

import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.reactivespringcloudstarter.property.MafProjectProperties
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.system.JavaVersion
import org.springframework.context.ApplicationContext
import org.springframework.util.StopWatch
import java.time.Instant
import java.time.ZoneId

/**
 * # SpringBootStartupHelper
 *
 * Description: SpringBootStartupHelper, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 10:04 PM
 */
class SpringBootStartupHelper(
    private val mafProjectProperties: MafProjectProperties,
    private val ipHelper: IpHelper,
    private val applicationContext: ApplicationContext
) : DisposableBean {
    companion object {
        @Suppress("HttpUrlsUsage")
        private val TEMPLATE: String =
            """

            ☕ ️️Powered by Kotlin :: v${KotlinVersion.CURRENT}, Java :: v${JavaVersion.getJavaVersion()}
            🥳 Congratulations! 🎉
            🖥 {}@{} started!
            ⚙️ Environment: {}
            ⏳ Deployment duration: {} seconds ({} ms)
            ⏰ App started at {} (timezone - {})
              App running at
              - Local:   http://localhost:{}{}/
              - Network: http://{}:{}{}/
            """.trimIndent()
        private val log = logger()
    }
    fun stop(stopWatch: StopWatch) {
        stopWatch.stop()
        log.info(
            TEMPLATE,
            mafProjectProperties.projectArtifactId, mafProjectProperties.version,
            mafProjectProperties.environment,
            stopWatch.totalTimeSeconds, stopWatch.totalTimeMillis,
            Instant.now().atZone(ZoneId.systemDefault()), ZoneId.systemDefault(),
            ipHelper.serverPort, mafProjectProperties.contextPath,
            ipHelper.getPublicIp(), ipHelper.serverPort, mafProjectProperties.contextPath
        )
        applicationContext.autowireCapableBeanFactory.destroyBean(this)
    }

    override fun destroy() {
        log.warn("Destroyed bean: ${this.javaClass.simpleName}")
    }
}

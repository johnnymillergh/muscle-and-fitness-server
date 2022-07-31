@file:Suppress("UnstableApiUsage")

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import java.time.LocalDateTime

description = "Muscle and Fitness Server :: OSS Center - Bootstrap"

plugins {
    id("com.google.cloud.tools.jib")
}

val versionDetails: Closure<VersionDetails> by extra
val gitVersionDetails = versionDetails()

dependencies {
    // MAF dependencies
    api(project(":oss-center-web"))
    api(project(":oss-center-message"))
}

tasks.withType<BootJar> {
    this.enabled = true
    // archiveFileName = [baseName]-[gitHash]-[version]-[classifier].[extension]
    this.archiveFileName.set("${archiveBaseName.get()}-${gitVersionDetails.gitHash}-${archiveVersion.get()}.${archiveExtension.get()}")
    logger.info("Building Spring Boot executable jar: ${this.archiveFileName.get()}")
}

tasks.withType<BootRun> {
    if (project.hasProperty("jvmArgs")) {
        val jvmArgsProperty = (project.properties["jvmArgs"] as String)
        logger.info("jvmArgsProperty for the app [${project.name}] (before split): `$jvmArgsProperty`")
        jvmArgs = jvmArgsProperty.split(Regex("\\s+"))
        logger.info("allJvmArgs for the app [${project.name}] (after split): $allJvmArgs")
    }
}

// https://www.baeldung.com/spring-boot-auto-property-expansion
// https://github.com/gradle/kotlin-dsl-samples/blob/master/samples/copy/build.gradle.kts
// https://www.tristanfarmer.dev/blog/gradle_property_expansion_spring_boot
tasks.withType<ProcessResources> {
    // Only expand the file `application.yml`
    filesMatching("**/application.yml") {
        expand(project.properties)
    }
}

springBoot {
    buildInfo()
}

// https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin
jib {
    pluginExtensions {
        pluginExtension {
            implementation = "com.google.cloud.tools.jib.gradle.extension.springboot.JibSpringBootExtension"
            properties = mapOf("useDeprecatedExcludeDevtoolsOption" to "true")
        }
    }
    val temurinTag: String by properties
    from.image = "eclipse-temurin:${temurinTag}"
    val dockerHubRepositoryPrefix: String by properties
    val projectArtifactId: String by properties
    val ossCenterArtifactId: String by properties
    to.image = "$dockerHubRepositoryPrefix$projectArtifactId.$ossCenterArtifactId"
    to.tags = setOf("${gitVersionDetails.gitHash}-${project.version}")
    container.appRoot = "/$ossCenterArtifactId"
    val projectBuildSourceEncoding: String by properties
    container.jvmFlags = listOf("-Dfile.encoding=$projectBuildSourceEncoding")
    val ossCenterPort: String by properties
    container.ports = listOf(ossCenterPort)
    container.creationTime = LocalDateTime.now().toString()
}

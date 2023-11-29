@file:Suppress("UnstableApiUsage")

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import java.time.ZonedDateTime

description = "Muscle and Fitness Server :: Spring Boot Admin - Bootstrap"

plugins {
    id("com.google.cloud.tools.jib")
}

val versionDetails: Closure<VersionDetails> by extra
val gitVersionDetails = versionDetails()

dependencies {
    // MAF dependencies
    implementation(project(":spring-cloud-starter")){
        exclude("de.codecentric", "spring-boot-admin-starter-client")
    }
    implementation(libs.spring.boot.admin.server.get())
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
            properties = mapOf("excludeDevtools" to "true")
        }
    }
    from.image = "${libs.versions.jreImage.get()}:${libs.versions.jreTag.get()}"
    val dockerHubRepositoryPrefix: String by project
    val projectArtifactId: String by project
    val springBootAdminArtifactId: String by project
    to.image = "$dockerHubRepositoryPrefix$projectArtifactId.$springBootAdminArtifactId"
    to.tags = setOf("${gitVersionDetails.gitHash}-${project.version}")
    container.appRoot = "/$springBootAdminArtifactId"
    val projectBuildSourceEncoding: String by project
    container.jvmFlags = listOf("-Dfile.encoding=$projectBuildSourceEncoding")
    val springBootAdminPort: String by project
    container.ports = listOf(springBootAdminPort)
    // container.creationTime should be an ISO 8601 date-time (see DateTimeFormatter.ISO_DATE_TIME)
    // or a special keyword ("EPOCH", "USE_CURRENT_TIMESTAMP"): 2022-08-01T06:53:20.970244
    container.creationTime.set(ZonedDateTime.now().toString())
}

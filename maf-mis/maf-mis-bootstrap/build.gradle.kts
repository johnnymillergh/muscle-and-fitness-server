@file:Suppress("UnstableApiUsage")

import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun
import java.time.ZonedDateTime

description = "Muscle and Fitness Server :: MAF MIS - Bootstrap"

plugins {
    id("com.google.cloud.tools.jib")
}

val versionDetails: Closure<VersionDetails> by extra
val gitVersionDetails = versionDetails()

dependencies {
    implementation(project(":maf-mis:maf-mis-web"))
    implementation(project(":maf-mis:maf-mis-message"))
    /*
    ! WARNING: ShardingSphere uses snakeyaml 1.3.3, yet Spring Boot only depends on 1.3.0.
    ! WARNING: So we need to constraint the version of snakeyaml to 1.3.3
    FIXME: Once Spring Boot upgrades snakeyaml to higher version (>= 1.3.3), we can remove this constraint
    */
    implementation(libs.snakeyaml.get().let { "${it.module}" }) {
        this.version {
            this.strictly(libs.versions.snakeyaml.get())
        }
    }
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
    from.image = "eclipse-temurin:${libs.versions.temurinTag}"
    val projectArtifactId: String by project
    val mafMisArtifactId: String by project
    to.image = "${libs.versions.dockerHubRepositoryPrefix}$projectArtifactId.$mafMisArtifactId"
    to.tags = setOf("${gitVersionDetails.gitHash}-${project.version}")
    container.appRoot = "/$mafMisArtifactId"
    val projectBuildSourceEncoding: String by project
    container.jvmFlags = listOf("-Dfile.encoding=$projectBuildSourceEncoding")
    val mafMisPort: String by project
    container.ports = listOf(mafMisPort)
    // container.creationTime should be an ISO 8601 date-time (see DateTimeFormatter.ISO_DATE_TIME)
    // or a special keyword ("EPOCH", "USE_CURRENT_TIMESTAMP"): 2022-08-01T06:53:20.970244
    container.creationTime.set(ZonedDateTime.now().toString())
}

@file:Suppress("UnstableApiUsage")

import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

description = "Muscle and Fitness Server :: Auth Center - Bootstrap"

tasks.withType<BootJar> {
    this.enabled = true
}

springBoot {
    buildInfo()
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

dependencies {
    // MAF dependencies, and the dependencies in `bootstrap` have no need to be transitive
    implementation(project(":auth-center-web"))
    implementation(project(":auth-center-message"))
}

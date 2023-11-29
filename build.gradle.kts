@file:Suppress(
    // https://youtrack.jetbrains.com/issue/KTIJ-19369
    // https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-cant-be-called-in-this-context-by-implicit-receiver-with-plugins-in-Gradle-version-catalogs-as-a-TOML-file#focus=Comments-27-5860112.0-0
    // to suppress error for `alias(libs.plugins.kotlin.jvm)`
    "DSL_SCOPE_VIOLATION"
)

import enforcer.rules.RequireGradleVersion
import enforcer.rules.RequireJavaVendor
import enforcer.rules.RequireJavaVersion
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    idea
    `java-library`
    jacoco
    `maven-publish`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.plugin.spring) apply false
    alias(libs.plugins.spring.boot) apply false
    // https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
    alias(libs.plugins.spring.dependency.management) apply false
    alias(libs.plugins.jib) apply false
    alias(libs.plugins.git.version)
    alias(libs.plugins.versions)
    alias(libs.plugins.project.enforcer)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

// Disable building any artifacts for the rootProject
tasks.withType<Jar> {
    this.enabled = false
}
tasks.withType<BootJar> {
    this.enabled = false
}

buildscript {
    dependencies {
        // https://github.com/GoogleContainerTools/jib-extensions/blob/master/first-party/jib-spring-boot-extension-gradle/README.md
        classpath("com.google.cloud.tools:jib-spring-boot-extension-gradle:0.1.0")
    }
}

allprojects {
    val projectGroupId: String by project
    group = projectGroupId
    val projectVersion: String by project
    version = projectVersion

    apply {
        plugin("java")
        plugin("java-library")
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    val projectGroupId: String by project
    group = projectGroupId
    val projectVersion: String by project
    version = projectVersion

    apply {
        plugin("java")
        plugin("java-library")
        plugin("jacoco")
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.springframework.boot")
        // https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
        plugin("io.spring.dependency-management")
        plugin("com.palantir.git-version")
        plugin("com.github.ben-manes.versions")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        // Run the compiler as a separate process.
        // https://docs.gradle.org/current/userguide/performance.html#compiler_daemon
        options.isFork = true
    }

    // https://docs.gradle.org/current/userguide/performance.html#parallel_test_execution
    tasks.withType<Test>().configureEach {
        // The normal approach is to use some number less than or equal to the number of CPU cores you have,
        // such as this algorithm:
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
        // To fork a new test VM after a certain number of tests have run
        setForkEvery(100)
    }

    // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html
    tasks.withType<Test> {
        // Configuration parameters to execute top-level classes in parallel but methods in the same thread
        // https://www.jvt.me/posts/2021/03/11/gradle-speed-parallel/
        systemProperties["junit.jupiter.execution.parallel.enabled"] = "true"
        systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
        systemProperties["junit.jupiter.execution.parallel.mode.classes.default"] = "concurrent"
        // Discover and execute JUnit Platform-based tests
        useJUnitPlatform()
        failFast = true
        // https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
        testLogging {
            // set options for log level LIFECYCLE
            events = mutableSetOf(FAILED, PASSED, SKIPPED, STANDARD_OUT)
            exceptionFormat = FULL
            showStandardStreams = true
            showExceptions = true
            showCauses = true
            showStackTraces = true
            // set options for log level DEBUG and INFO
            debug {
                events = mutableSetOf(FAILED, PASSED, SKIPPED, STANDARD_OUT)
                exceptionFormat = FULL
            }
            info {
                events = debug.events
                exceptionFormat = debug.exceptionFormat
            }
        }
        // report is always generated after tests run
        finalizedBy(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        // tests are required to run before generating the report
        dependsOn(tasks.test)
        reports {
            csv.required.set(true)
        }
    }

    // Disable for take of building Spring Boot executable jar for most of the subprojects,
    // Only bootstrap subproject needs it to be `true`.
    tasks.withType<BootJar> { this.enabled = false }

    // Execution failed for task ':spring-cloud-starter:jar'.
    //> Entry com/jmsoftware/maf/springcloudstarter/quartz/converter/QuartzJobConfigurationMapStructMapperImpl.class is
    // a duplicate but no duplicate handling strategy has been set. Please refer to
    // https://docs.gradle.org/7.6/dsl/org.gradle.api.tasks.Copy.html#org.gradle.api.tasks.Copy:duplicatesStrategy for details.
    tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.WARN }

    dependencies {
        implementation(platform(rootProject.libs.spring.boot.bom))
        implementation(platform(rootProject.libs.spring.cloud.bom))

        // Kotlin
        implementation(kotlin("reflect"))
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // Tools
        implementation(rootProject.libs.guava.get())
        implementation(rootProject.libs.hutool.get())
        implementation(rootProject.libs.mapstruct.asProvider().get())
        // https://mapstruct.org/documentation/stable/reference/html/#_gradle
        // https://github.com/mapstruct/mapstruct-examples/blob/main/mapstruct-kotlin-gradle/build.gradle.kts
        kapt(rootProject.libs.mapstruct.processor.get())
        // https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html#appendix.configuration-metadata.annotation-processor.configuring
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        // Testing
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation(rootProject.testLibs.mockito.kotlin.get())
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }
}

enforce {
    rule(RequireGradleVersion::class.java) {
        this.setEnforcerLevel("ERROR")
        this.setProperty("version", "[${libs.versions.gradle.get()}]")
    }
    rule(RequireJavaVendor::class.java) {
        this.setEnforcerLevel("ERROR")
        this.include(libs.versions.javaVendor.get())
    }
    rule(RequireJavaVersion::class.java) {
        this.setEnforcerLevel("ERROR")
        this.setProperty("version", "[${libs.versions.javaVersion.get()},)")
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

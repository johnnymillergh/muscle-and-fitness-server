import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.JavaVersion.VERSION_1_8
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    idea
    `java-library`
    `maven-publish`
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    id("org.springframework.boot") apply false
    // https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
    id("io.spring.dependency-management") apply false
    id("com.google.cloud.tools.jib") apply false
    id("com.palantir.git-version")
    id("com.github.ben-manes.versions")
}

java.sourceCompatibility = VERSION_1_8
java.targetCompatibility = VERSION_1_8

// To disable build any artifacts for the rootProject
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
        plugin("kotlin")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.springframework.boot")
        // https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
        plugin("io.spring.dependency-management")
        plugin("com.palantir.git-version")
        plugin("com.github.ben-manes.versions")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        // Run the compiler as a separate process, https://docs.gradle.org/current/userguide/performance.html#compiler_daemon
        options.isFork = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // https://docs.gradle.org/current/userguide/performance.html#parallel_test_execution
    tasks.withType<Test>().configureEach {
        // The normal approach is to use some number less than or equal to the number of CPU cores you have, such as this algorithm:
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
        // To fork a new test VM after a certain number of tests have run
        setForkEvery(100)
    }

    // Disable for take of building Spring Boot executable jar for most of the subprojects,
    // Only bootstrap subproject need it to be `true`.
    tasks.withType<BootJar> {
        this.enabled = false
    }

    // https://github.com/gradle/kotlin-dsl-samples/issues/1002
    configure<DependencyManagementExtension> {
        imports {
            val springBootVersion: String by project
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            val springCloudVersion: String by project
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
        }
    }

    dependencies {
        val guavaVersion: String by project
        val hutoolVersion: String by project
        val functionaljavaVersion: String by project
        val functionaljava8Version: String by project
        val mapstructVersion: String by project
        val mockitoKotlinVersion: String by project

        // Kotlin
        implementation(kotlin("stdlib-jdk8"))
        implementation(kotlin("reflect"))
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        // Tools
        implementation("com.google.guava:guava:$guavaVersion")
        implementation("cn.hutool:hutool-all:$hutoolVersion")
        implementation("org.functionaljava:functionaljava:$functionaljavaVersion")
        implementation("org.functionaljava:functionaljava-java8:$functionaljava8Version")
        implementation("org.functionaljava:functionaljava-quickcheck:$functionaljavaVersion")
        implementation("org.functionaljava:functionaljava-java-core:$functionaljavaVersion")
        implementation("org.mapstruct:mapstruct:$mapstructVersion")

        // TODO: this dependency might not be here for global use
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        // Testing
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
        testImplementation("org.jacoco:org.jacoco.agent:0.8.8")
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

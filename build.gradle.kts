import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.JavaVersion.VERSION_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }
    dependencies {
        classpath("io.spring.gradle:dependency-management-plugin:1.0.12.RELEASE")
    }
}

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
}

java.sourceCompatibility = VERSION_17
java.targetCompatibility = VERSION_17

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

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
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
        //plugin("kotlin")
        //plugin("org.jetbrains.kotlin.plugin.spring")
        // https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
        plugin("io.spring.dependency-management")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
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

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val implementation by configurations
    val annotationProcessor by configurations
    val testImplementation by configurations
    dependencies {
        val guavaVersion: String by project
        val hutoolVersion: String by project
        val functionaljavaVersion: String by project
        val functionaljava8Version: String by project
        val mapstructVersion: String by project

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

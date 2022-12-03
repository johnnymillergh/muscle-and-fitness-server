rootProject.name = "muscle-and-fitness-server"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val jibVersion: String by settings
    val gradleGitVersion: String by settings
    val gradleVersionsPluginVersion: String by settings
    val gradleProjectEnforcerVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("kapt") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.allopen") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("com.google.cloud.tools.jib") version jibVersion
        id("com.palantir.git-version") version gradleGitVersion
        id("com.github.ben-manes.versions") version gradleVersionsPluginVersion
        id("org.kordamp.gradle.project-enforcer") version gradleProjectEnforcerVersion
    }
}

// Infrastructures
include(":spring-cloud-starter")
include(":reactive-spring-cloud-starter")
include(":common")
include(":universal-ui")

// Auth Center
include(":auth-center:auth-center-infra")
include(":auth-center:auth-center-domain")
include(":auth-center:auth-center-biz")
include(":auth-center:auth-center-message")
include(":auth-center:auth-center-web")
include(":auth-center:auth-center-bootstrap")

// API Gateway
include(":api-gateway:api-gateway-infra")
include(":api-gateway:api-gateway-biz")
include(":api-gateway:api-gateway-domain")
include(":api-gateway:api-gateway-message")
include(":api-gateway:api-gateway-web")
include(":api-gateway:api-gateway-bootstrap")

// MAF MIS
include(":maf-mis:maf-mis-infra")
include(":maf-mis:maf-mis-domain")
include(":maf-mis:maf-mis-biz")
include(":maf-mis:maf-mis-message")
include(":maf-mis:maf-mis-web")
include(":maf-mis:maf-mis-bootstrap")

// OSS Center
include(":oss-center:oss-center-infra")
include(":oss-center:oss-center-domain")
include(":oss-center:oss-center-biz")
include(":oss-center:oss-center-message")
include(":oss-center:oss-center-web")
include(":oss-center:oss-center-bootstrap")

// Spring Boot Admin
include(":spring-boot-admin:spring-boot-admin-bootstrap")

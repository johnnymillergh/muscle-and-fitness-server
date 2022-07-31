rootProject.name = "muscle-and-fitness-server"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
    }
}

include(":spring-cloud-starter")
include(":reactive-spring-cloud-starter")
include(":common")
include(":universal-ui")

include(":maf-mis-infra")
include(":maf-mis-biz")
include(":maf-mis-domain")
include(":maf-mis-bootstrap")
include(":maf-mis-message")
include(":maf-mis-web")

include(":oss-center-biz")
include(":oss-center-domain")
include(":oss-center-infra")
include(":oss-center-bootstrap")
include(":oss-center-web")
include(":oss-center-message")

include(":auth-center-bootstrap")
include(":auth-center-domain")
include(":auth-center-web")
include(":auth-center-biz")
include(":auth-center-infra")
include(":auth-center-message")

include(":api-gateway-biz")
include(":api-gateway-domain")
include(":api-gateway-infra")
include(":api-gateway-bootstrap")
include(":api-gateway-message")
include(":api-gateway-web")

include(":spring-boot-admin-bootstrap")

project(":maf-mis-infra").projectDir = file("maf-mis/maf-mis-infra")
project(":oss-center-biz").projectDir = file("oss-center/oss-center-biz")
project(":api-gateway-biz").projectDir = file("api-gateway/api-gateway-biz")
project(":auth-center-bootstrap").projectDir = file("auth-center/auth-center-bootstrap")
project(":auth-center-domain").projectDir = file("auth-center/auth-center-domain")
project(":auth-center-web").projectDir = file("auth-center/auth-center-web")
project(":maf-mis-biz").projectDir = file("maf-mis/maf-mis-biz")
project(":maf-mis-domain").projectDir = file("maf-mis/maf-mis-domain")
project(":oss-center-domain").projectDir = file("oss-center/oss-center-domain")
project(":oss-center-infra").projectDir = file("oss-center/oss-center-infra")
project(":api-gateway-message").projectDir = file("api-gateway/api-gateway-message")
project(":auth-center-infra").projectDir = file("auth-center/auth-center-infra")
project(":oss-center-bootstrap").projectDir = file("oss-center/oss-center-bootstrap")
project(":api-gateway-domain").projectDir = file("api-gateway/api-gateway-domain")
project(":api-gateway-infra").projectDir = file("api-gateway/api-gateway-infra")
project(":api-gateway-bootstrap").projectDir = file("api-gateway/api-gateway-bootstrap")
project(":maf-mis-web").projectDir = file("maf-mis/maf-mis-web")
project(":api-gateway-web").projectDir = file("api-gateway/api-gateway-web")
project(":auth-center-message").projectDir = file("auth-center/auth-center-message")
project(":maf-mis-bootstrap").projectDir = file("maf-mis/maf-mis-bootstrap")
project(":spring-boot-admin-bootstrap").projectDir = file("spring-boot-admin/spring-boot-admin-bootstrap")
project(":oss-center-web").projectDir = file("oss-center/oss-center-web")
project(":auth-center-biz").projectDir = file("auth-center/auth-center-biz")
project(":oss-center-message").projectDir = file("oss-center/oss-center-message")
project(":maf-mis-message").projectDir = file("maf-mis/maf-mis-message")

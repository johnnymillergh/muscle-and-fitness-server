@file:Suppress("UnstableApiUsage")

rootProject.name = "muscle-and-fitness-server"

dependencyResolutionManagement {
    versionCatalogs {
        create("testLibs") {
            from(files("gradle/test-libs.versions.toml"))
        }
    }
}

include(
    // Infrastructures
    ":spring-cloud-starter",
    ":reactive-spring-cloud-starter",
    ":common",
    ":universal-ui",
    // Auth Center
    "auth-center:auth-center-infra",
    "auth-center:auth-center-domain",
    "auth-center:auth-center-biz",
    "auth-center:auth-center-message",
    "auth-center:auth-center-web",
    "auth-center:auth-center-bootstrap",
    // API Gateway
    "api-gateway:api-gateway-infra",
    "api-gateway:api-gateway-biz",
    "api-gateway:api-gateway-domain",
    "api-gateway:api-gateway-message",
    "api-gateway:api-gateway-web",
    "api-gateway:api-gateway-bootstrap",
    // MAF MIS
    "maf-mis:maf-mis-infra",
    "maf-mis:maf-mis-domain",
    "maf-mis:maf-mis-biz",
    "maf-mis:maf-mis-message",
    "maf-mis:maf-mis-web",
    "maf-mis:maf-mis-bootstrap",
    // OSS Center
    "oss-center:oss-center-infra",
    "oss-center:oss-center-domain",
    "oss-center:oss-center-biz",
    "oss-center:oss-center-message",
    "oss-center:oss-center-web",
    "oss-center:oss-center-bootstrap",
    // Spring Boot Admin
    "spring-boot-admin:spring-boot-admin-bootstrap"
)

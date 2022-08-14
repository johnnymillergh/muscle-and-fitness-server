import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "Muscle and Fitness Server :: Reactive Spring Cloud Starter"

tasks.withType<BootJar> {
    this.enabled = false
}

configurations.implementation {
    isTransitive = true
}
configurations.runtimeOnly {
    isTransitive = true
}

dependencies {
    // MAF dependencies
    api(project(":common"))
    api(project(":universal-ui"))

    // Spring dependencies
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-autoconfigure")
    api("org.springframework.boot:spring-boot-autoconfigure-processor")
    api("org.springframework.boot:spring-boot-configuration-processor")
    api("org.springframework.cloud:spring-cloud-starter-consul-all")
    api("org.springframework.cloud:spring-cloud-starter-sleuth")
    api("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    api("org.apache.commons:commons-pool2:2.11.1")
    developmentOnly("org.springframework.boot:spring-boot-devtools") {
        isTransitive = true
    }
    // Optional Spring dependencies for other features
    compileOnly("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // Spring enhancement dependencies
    val springBootAdminStarterVersion: String by project
    api("de.codecentric:spring-boot-admin-starter-client:$springBootAdminStarterVersion")
    val springDocVersion: String by project
    api("org.springdoc:springdoc-openapi-webflux-ui:$springDocVersion")
    api("org.springdoc:springdoc-openapi-kotlin:$springDocVersion")
    val logstashLogbackEncoderVersion: String by project
    api("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")
}

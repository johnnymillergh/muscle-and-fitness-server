import org.springframework.boot.gradle.tasks.bundling.BootJar

description = "Muscle and Fitness Server :: Spring Cloud Starter"

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
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.springframework.boot:spring-boot-configuration-processor")
    api("org.springframework.boot:spring-boot-autoconfigure")
    api("org.springframework.boot:spring-boot-autoconfigure-processor")
    api("org.springframework.cloud:spring-cloud-starter-consul-all")
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
    api("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    api("io.github.openfeign:feign-okhttp")
    api("org.springframework.cloud:spring-cloud-starter-sleuth")
    api("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    api("org.springframework.boot:spring-boot-starter-integration")
    api("org.apache.commons:commons-pool2")
    developmentOnly("org.springframework.boot:spring-boot-devtools") {
        isTransitive = true
    }
    // Optional Spring dependencies for other features
    compileOnly("org.springframework.boot:spring-boot-starter-data-redis")
    compileOnly("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    compileOnly("org.springframework.boot:spring-boot-starter-amqp")
    compileOnly("org.springframework.boot:spring-boot-starter-quartz")
    compileOnly("org.springframework.boot:spring-boot-starter-websocket")
    compileOnly("org.springframework.boot:spring-boot-starter-reactor-netty")
    compileOnly("org.springframework.integration:spring-integration-redis")
    compileOnly("org.springframework.integration:spring-integration-sftp")

    // Spring enhancement dependencies
    val springBootAdminStarterVersion: String by project
    api("de.codecentric:spring-boot-admin-starter-client:$springBootAdminStarterVersion")
    val springDocVersion: String by project
    api("org.springdoc:springdoc-openapi-ui:$springDocVersion")
    api("org.springdoc:springdoc-openapi-kotlin:$springDocVersion")
    val logstashLogbackEncoderVersion: String by project
    api("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")

    val minioVersion: String by project
    compileOnly("io.minio:minio:$minioVersion")

    // ORM libraries
    val mybatisPlusBootStarterVersion: String by project
    compileOnly("com.baomidou:mybatis-plus-boot-starter:$mybatisPlusBootStarterVersion")
    val shardingsphereVersion: String by project
    runtimeOnly("org.apache.shardingsphere:shardingsphere-jdbc-core-spring-boot-starter:$shardingsphereVersion")

    // POI
    val poiVersion: String by project
    compileOnly("org.apache.poi:poi:$poiVersion")
    compileOnly("org.apache.poi:poi-ooxml:$poiVersion")
}

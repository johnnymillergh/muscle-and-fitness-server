description = "Muscle and Fitness Server :: Spring Cloud Starter"

val projectGroupId: String by project
group = projectGroupId
val projectVersion: String by project
version = projectVersion

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    // https://docs.spring.io/dependency-management-plugin/docs/current/reference/html/
    id("io.spring.dependency-management")
}

val developmentOnly = configurations.create("developmentOnly")
configurations.runtimeClasspath.get().extendsFrom(developmentOnly)

dependencies {
    // MAF dependencies
    implementation(project(":common"))

    // Spring dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-autoconfigure-processor")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-all")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    implementation("io.github.openfeign:feign-okhttp")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.apache.commons:commons-pool2")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
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
    implementation("de.codecentric:spring-boot-admin-starter-client:$springBootAdminStarterVersion")
    val springDocVersion: String by project
    implementation("org.springdoc:springdoc-openapi-ui:$springDocVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springDocVersion")
    val logstashLogbackEncoderVersion: String by project
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")

    val minioVersion: String by project
    compileOnly("io.minio:minio:$minioVersion")

    // ORM libraries
    val mybatisPlusBootStarterVersion: String by project
    compileOnly("com.baomidou:mybatis-plus-boot-starter:$mybatisPlusBootStarterVersion")
    val shardingsphereVersion: String by project
    compileOnly("org.apache.shardingsphere:shardingsphere-jdbc-core-spring-boot-starter:$shardingsphereVersion")

    // POI
    val poiVersion: String by project
    compileOnly("org.apache.poi:poi:$poiVersion")
    compileOnly("org.apache.poi:poi-ooxml:$poiVersion")
}

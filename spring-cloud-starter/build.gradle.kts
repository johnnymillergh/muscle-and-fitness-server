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
    api(libs.spring.boot.admin.client.get())
    api(libs.springdoc.openapi.ui.get())
    api(libs.springdoc.openapi.kotlin.get())
    api(libs.logstash.logback.encoder.get())

    compileOnly(libs.minio.get())

    // ORM libraries
    runtimeOnly("mysql:mysql-connector-java")
    api(libs.mybatis.plus.get())
    // https://stackoverflow.com/questions/71680935/how-to-exclude-dependencies-from-gradle-version-catalog
    api(libs.shardingsphere.get().let { "${it.module}:${it.versionConstraint.requiredVersion}" }) {
        exclude("org.codehaus.groovy", "groovy")
    }

    // POI
    api(libs.poi.asProvider().get())
    api(libs.poi.ooxml.get())
}

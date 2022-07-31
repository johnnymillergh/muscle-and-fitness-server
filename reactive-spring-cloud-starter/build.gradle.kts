dependencies {
    implementation(project(":common"))
    implementation(project(":universal-ui"))
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter-security:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter-aop:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.2")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.2")
    implementation("org.springframework.boot:spring-boot-autoconfigure-processor:2.7.2")
    implementation("org.springframework.boot:spring-boot-configuration-processor:2.7.2")
    implementation("org.springframework.cloud:spring-cloud-starter-consul-all:3.1.0")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth:3.1.1")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin:3.1.1")
    implementation("de.codecentric:spring-boot-admin-starter-client:2.7.1")
    implementation("org.apache.commons:commons-pool2:2.11.1")
    implementation("net.logstash.logback:logstash-logback-encoder:7.2")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.9")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.9")
    runtimeOnly("org.springframework.boot:spring-boot-devtools:2.7.2")
    compileOnly("org.springframework.boot:spring-boot-starter-data-redis-reactive:2.7.2")
}

description = "Muscle and Fitness Server :: Reactive Spring Cloud Starter"

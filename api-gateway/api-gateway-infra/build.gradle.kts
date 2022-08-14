description = "Muscle and Fitness Server :: API Gateway - Infra"

configurations.implementation {
    isTransitive = true
}
configurations.runtimeOnly {
    isTransitive = true
}
configurations.testImplementation {
    isTransitive = true
}

dependencies {
    // MAF dependencies
    api(project(":reactive-spring-cloud-starter"))

    // Spring dependencies
    api("org.springframework.cloud:spring-cloud-starter-gateway")
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    val knife4jVersion: String by project
    api("com.github.xiaoymin:knife4j-springdoc-ui:$knife4jVersion")

    // Testing
    testImplementation("io.projectreactor:reactor-test")
}

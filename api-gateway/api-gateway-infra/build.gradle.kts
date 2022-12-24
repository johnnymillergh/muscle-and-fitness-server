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

    api(libs.knife4j.get())

    // Testing
    testImplementation("io.projectreactor:reactor-test")
}

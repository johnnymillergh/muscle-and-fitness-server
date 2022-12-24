description = "Muscle and Fitness Server :: OSS Center - Infra"

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
    api(project(":spring-cloud-starter"))

    // Spring dependencies
    api("org.springframework.boot:spring-boot-starter-data-redis")
    api("org.springframework:spring-test")

    // Spring integration
    api("org.springframework.integration:spring-integration-redis")

    api(libs.minio.get())
    api(libs.tika.core.get())

    // Testing
    testImplementation("org.mockito:mockito-inline")
}

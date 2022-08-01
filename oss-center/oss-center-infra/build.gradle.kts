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

    val minioVersion: String by project
    api("io.minio:minio:$minioVersion")
    val apacheTikaVersion: String by project
    api("org.apache.tika:tika-core:$apacheTikaVersion")

    // Testing
    testImplementation("org.mockito:mockito-inline")
}

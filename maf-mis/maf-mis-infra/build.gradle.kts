description = "Muscle and Fitness Server :: MAF MIS - Infra"

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
    api("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    api("org.springframework.boot:spring-boot-starter-amqp")
    api("org.springframework.boot:spring-boot-starter-quartz")
    api("org.springframework.boot:spring-boot-starter-websocket")
    api("org.springframework.boot:spring-boot-starter-reactor-netty")
    api("org.springframework:spring-test")

    // Spring integration
    api("org.springframework.integration:spring-integration-redis")

    // Apache POI - Java API To Access Microsoft Format Files
    api(libs.poi.asProvider().get())
    api(libs.poi.ooxml.get())

    // Testing
    testImplementation("org.springframework.amqp:spring-rabbit-test") {
        exclude("org.codehaus.groovy", "groovy")
    }
    testImplementation("org.mockito:mockito-inline")
}

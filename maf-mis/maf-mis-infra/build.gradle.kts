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

    // ORM Libraries
    // runtimeOnly libraries are the opposite, they are available at runtime but not at compile-time.
    runtimeOnly("mysql:mysql-connector-java")
    api(libs.mybatis.plus.get())
    // https://stackoverflow.com/questions/71680935/how-to-exclude-dependencies-from-gradle-version-catalog
    api(libs.shardingsphere.get().let { "${it.module}:${it.versionConstraint.requiredVersion}" }) {
        exclude("org.codehaus.groovy", "groovy")
    }

    // Apache POI - Java API To Access Microsoft Format Files
    api(libs.poi.asProvider().get())
    api(libs.poi.ooxml.get())

    // Testing
    testImplementation("org.springframework.amqp:spring-rabbit-test") {
        exclude("org.codehaus.groovy", "groovy")
    }
    testImplementation("org.mockito:mockito-inline")
}

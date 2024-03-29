description = "Muscle and Fitness Server :: Auth Center - Infra"

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
    val mybatisPlusBootStarterVersion: String by project
    api("com.baomidou:mybatis-plus-boot-starter:$mybatisPlusBootStarterVersion")
    val shardingsphereVersion: String by project
    api("org.apache.shardingsphere:shardingsphere-jdbc-core-spring-boot-starter:$shardingsphereVersion") {
        exclude("org.codehaus.groovy", "groovy")
    }

    // JWT, https://github.com/jwtk/jjwt
    // Cannot Remove jjwt dependencies, cuz the login process is in here `auth-center`, and the auth process is in `api-gateway`
    val jjwtVersion: String by project
    api("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    api("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    // Apache POI - Java API To Access Microsoft Format Files
    val poiVersion: String by project
    api("org.apache.poi:poi:$poiVersion")
    api("org.apache.poi:poi-ooxml:$poiVersion")

    // Testing
    testImplementation("org.springframework.amqp:spring-rabbit-test") {
        exclude("org.codehaus.groovy", "groovy")
    }
    testImplementation("org.mockito:mockito-inline")
}

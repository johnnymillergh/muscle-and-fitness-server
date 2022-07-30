description = "Muscle and Fitness Server :: Auth Center - Biz"

dependencies {
    // MAF dependencies
    implementation(project(":spring-cloud-starter"))
    implementation(project(":universal-ui"))

    // Spring Dependencies
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
    implementation("org.springframework:spring-test")

    // Spring Integration
    implementation("org.springframework.integration:spring-integration-redis")

    // ORM Libraries
    // runtimeOnly libraries are the opposite, they are available at runtime but not at compile-time.
    runtimeOnly("mysql:mysql-connector-java")
    val mybatisPlusBootStarterVersion: String by project
    implementation("com.baomidou:mybatis-plus-boot-starter:$mybatisPlusBootStarterVersion")
    val shardingsphereVersion: String by project
    implementation("org.apache.shardingsphere:shardingsphere-jdbc-core-spring-boot-starter:$shardingsphereVersion")

    // JWT, https://github.com/jwtk/jjwt
    // Cannot Remove jjwt dependencies, cuz the login process is in here `auth-center`, and the auth process is in `api-gateway`
    val jjwtVersion: String by project
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    // Apache POI - Java API To Access Microsoft Format Files
    val poiVersion: String by project
    runtimeOnly("org.apache.poi:poi:$poiVersion")
    runtimeOnly("org.apache.poi:poi-ooxml:$poiVersion")

    // Testing
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.mockito:mockito-inline")

    implementation(project(":auth-center-domain"))
}

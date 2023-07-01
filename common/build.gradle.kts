description = "Muscle and Fitness Server :: Common"

configurations {
    testImplementation.get().extendsFrom(compileOnly.get())
}

dependencies {
    compileOnly("org.springframework:spring-web")
    compileOnly("org.springframework.boot:spring-boot-starter-validation")
    compileOnly("org.springframework.boot:spring-boot-starter-security")
    compileOnly("org.springframework.boot:spring-boot-starter-json")
}

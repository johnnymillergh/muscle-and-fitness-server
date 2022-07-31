description = "Muscle and Fitness Server :: Universal UI"

tasks.withType<Jar> {
    from("src/main/resources") {
        into("META-INF/resources/")
    }
}

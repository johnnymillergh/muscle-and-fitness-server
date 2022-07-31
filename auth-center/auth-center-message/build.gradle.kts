description = "Muscle and Fitness Server :: Auth Center - Message"

val projectGroupId: String by project
group = projectGroupId
val projectVersion: String by project
version = projectVersion

dependencies {
    // MAF dependencies
    api(project(":auth-center-biz"))
}

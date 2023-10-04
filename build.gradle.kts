plugins {
    id("java")
}

group = "com.vitindev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    implementation("redis.clients:jedis:3.9.0")
    implementation("com.google.code.gson:gson:2.9.0")

}

tasks.test {
    useJUnitPlatform()
}
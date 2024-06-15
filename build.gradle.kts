plugins {
    id("java")
}

group = "com.vitindev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("redis.clients:jedis:3.7.0")
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.esotericsoftware:kryo:5.5.0")


}

tasks.test {
    useJUnitPlatform()
}
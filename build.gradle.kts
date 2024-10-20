plugins {
    java
    id("io.quarkus")
}

group = "io.intellij.devops.ddns"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public/") }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    testCompileOnly {
        extendsFrom(configurations.testAnnotationProcessor.get())
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    // implementation("io.quarkus:quarkus-resteasy")
    // implementation("io.quarkus:quarkus-resteasy-jackson")

    // 更高性能的响应式
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")

    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-hibernate-validator")

    // problem like: https://github.com/spring-attic/spring-native/issues/429
    // implementation("io.quarkiverse.logging.logback:quarkus-logging-logback:1.1.2")
    // // https://mvnrepository.com/artifact/io.quarkiverse.logging.logback/quarkus-logging-logback-deployment
    // implementation("io.quarkiverse.logging.logback:quarkus-logging-logback-deployment:1.1.2")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("org.apache.commons:commons-collections4:4.5.0-M2")

    implementation("com.aliyun:alidns20150109:3.4.4") {
        // exclude(group = "org.dom4j", module = "dom4j")
        exclude(group = "com.aliyun", module = "tea-xml")
    }
    // https://mvnrepository.com/artifact/org.dom4j/dom4j
    // implementation("org.dom4j:dom4j:2.1.4")

}


tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    id("jacoco")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

allprojects {
    group = property("app.group").toString()
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.cloud.dependencies.get().toString())
    }
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    testImplementation (libs.lombok)
    testAnnotationProcessor(libs.lombok)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    annotationProcessor(libs.spring.boot.configuration.processor)
    testImplementation(libs.spring.boot.starter.test)
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-jdbc")
    runtimeOnly ("com.h2database:h2")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    testImplementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1")
    implementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.8.1")

    // 낙관락 - 재시도
    implementation ("org.springframework.retry:spring-retry")

    // 분산락 - 레디스
    implementation ("org.springframework.boot:spring-boot-starter-data-redis")
    implementation ("org.redisson:redisson-spring-boot-starter:3.27.1")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.0")

    runtimeOnly ("com.mysql:mysql-connector-j")

    // testcontainer
    testImplementation ("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation ("org.testcontainers:testcontainers:1.20.3")
    testImplementation ("org.testcontainers:junit-jupiter:1.20.3")
    testImplementation ("org.testcontainers:mysql:1.20.3")

    // Kafka
    implementation ("org.springframework.kafka:spring-kafka")
    testImplementation("org.testcontainers:kafka:1.17.6")
    testImplementation ("org.awaitility:awaitility:4.2.0")
}

// about source and compilation
java {
    sourceCompatibility = JavaVersion.VERSION_17
}

with(extensions.getByType(JacocoPluginExtension::class.java)) {
    toolVersion = "0.8.7"
}

// bundling tasks
tasks.getByName("bootJar") {
    enabled = true
}
tasks.getByName("jar") {
    enabled = false
}
// test tasks
tasks.test {
    ignoreFailures = true
    useJUnitPlatform()
}


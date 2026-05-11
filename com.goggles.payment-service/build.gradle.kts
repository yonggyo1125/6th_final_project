plugins {
    java
    id ("org.springframework.boot") version "3.5.13"
    id ("io.spring.dependency-management") version "1.1.7"
    id ("com.diffplug.spotless") version "6.25.0"
}

group = "com.goggles"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/9oogle/common-library")
        credentials {
            username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GPR_USER2")
            password = project.findProperty("gpr.token")?.toString() ?: System.getenv("GPR_TOKEN2")
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.2")
    }
}

dependencies {
    // ── 공용 라이브러리 ───────────────────────────────
    implementation("com.goggles:common-library:1.0.3")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation ("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-security")

    // ── 모니터링 (prometheus) ───────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // ── AOP / AspectJ (InboxAdvice @Aspect 용) ───────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework:spring-aspects")

    // ── JPA (소비자가 Spring Data JPA 를 사용한다고 가정) ────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")

    // ── Spring Web/MVC (GlobalExceptionHandler 용) ───────────────────────────
    implementation("org.springframework:spring-web")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("jakarta.servlet:jakarta.servlet-api")

    // ── Kafka (선택적 — 소비자가 spring-kafka 없으면 KafkaAutoConfig 비활성) ──
    implementation("org.springframework.kafka:spring-kafka")

    // eureka
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // ── Lombok ──────────────────────────────────────────────────────────────
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Micrometer Tracing
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // ── Test ─────────────────────────────────────────────────────────────────
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.kafka:spring-kafka")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    runtimeOnly("org.postgresql:postgresql")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
spotless {
    java {
        googleJavaFormat()
    }
}
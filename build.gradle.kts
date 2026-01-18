plugins {
    java
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.spring") version "2.3.0"
    id("org.springframework.boot") version "4.0.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.11.3"
}

group = "santannaf.spring4.oracle"
version = "0.0.1"
description = "demo-spring4-oracle"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.23.0")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("com.oracle.database.jdbc:ojdbc11")

    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")

    implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")

    testImplementation("org.springframework.boot:spring-boot-starter-data-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.bootJar {
    archiveFileName.set("app.jar")
}

tasks.register<Exec>("runCustomJar") {
    group = "application"
    description = "Run custom jar"
    dependsOn("bootJar")
    val appName = "app.jar"
    val addressesBuild = "./build/libs/$appName"
    commandLine(
        "java",
        "-agentlib:native-image-agent=config-merge-dir=./src/main/resources/META-INF/native-image/",
        "-jar",
        addressesBuild
    )
}

graalvmNative {
    binaries {
        named("main") {
            imageName.set("app")
            verbose.set(true)
            debug.set(true)
            configurationFileDirectories.from(file("src/main/resources/META-INF/native-image"))
            buildArgs(
                "--color=always",
                "-H:+AddAllCharsets",
                "-J-Dfile.encoding=UTF-8",
                "-J-Duser.language=pt",
                "-J-Duser.country=BR",
                "-J-Duser.timezone=America/Sao_Paulo"
            )
        }
    }
}
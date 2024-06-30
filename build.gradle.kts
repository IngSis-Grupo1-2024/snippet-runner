import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

val ktlint by configurations.creating
group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/IngSis-Grupo1-2024/PrintScript")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }

    maven {
        name = "GitHubPackagesClassRedisStreams"
        url = uri("https://maven.pkg.github.com/austral-ingsis/class-redis-streams")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }

    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/IngSis-Grupo1-2024/redis-events")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    ktlint("com.pinterest.ktlint:ktlint-cli:1.2.1") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-rest:3.2.1")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.postgresql:postgresql:42.7.3")
    // PrintScript modules:
    implementation("PrintScript:app:1.1.0-SNAPSHOT")
    implementation("PrintScript:cli:1.1.0-SNAPSHOT")
    implementation("PrintScript:parser:1.1.0-SNAPSHOT")
    implementation("PrintScript:lexer:1.1.0-SNAPSHOT")
    implementation("PrintScript:interpreter:1.1.0-SNAPSHOT")
    implementation("PrintScript:formatter:1.1.0-SNAPSHOT")
    implementation("PrintScript:sca:1.1.0-SNAPSHOT")
    implementation("PrintScript:components:1.1.0-SNAPSHOT")
    implementation("PrintScript:utils:1.1.0-SNAPSHOT")
    // for swagger to use the requests as asset-service:
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.7.0")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.7.0")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.7.0")
    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.austral.ingsis:redis-streams-mvc:0.1.13")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    // auth0 impl
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    // redis events
    implementation("org.gradle.redisevents:events:1.1.0-SNAPSHOT")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}

tasks.check {
    dependsOn(ktlintCheck)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    args(
        "-F",
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}

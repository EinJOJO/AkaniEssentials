plugins {
    id("java")
    alias(libs.plugins.shadow)
}

group = "it.einjojo.akani"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "LiteCommands"
        url = uri("https://repo.panda-lang.org/releases")
    }
}

dependencies {
    compileOnly(libs.akanicore) // clone https://github.com/EinJojo/AkaniCore and run `./gradlew api:publishMavenLocal` so that this works
    compileOnly(libs.paper)
    implementation(libs.litecommands)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<Jar> {
        enabled = false
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(mapOf("version" to project.version.toString()))
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.isIncremental = true
        options.compilerArgs.add("-parameters")
    }

    build {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveClassifier.set("")
    }
}
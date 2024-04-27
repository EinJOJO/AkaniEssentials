plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
}

group = "it.einjojo.akani"
version = "1.4.0"

repositories {

    mavenCentral()
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.akani.dev/releases")
    maven("https://repo.oraxen.com/releases")
}

dependencies {
    compileOnly(libs.akanicore) // clone https://github.com/EinJojo/AkaniCore and run `./gradlew api:publishMavenLocal` so that this works
    compileOnly(libs.caffeine)
    compileOnly(libs.paper)
    implementation(libs.acf)
    implementation(libs.fastboard)
    annotationProcessor(libs.acf)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
    withType<Jar> {
        enabled = true
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

    assemble {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveClassifier.set("")
        archiveVersion.set("")
        relocate("co.aikar.commands", "it.einjojo.akani.essentials.command.acf")
        relocate("fr.mrmicky.fastboard", "it.einjojo.akani.essentials.scoreboard.fastboard")

    }
}
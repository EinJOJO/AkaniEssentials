plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
    kotlin("jvm")
}

group = "it.einjojo.akani"
version = "1.6.0"

repositories {

    mavenCentral()
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.akani.dev/releases")
    maven("https://repo.oraxen.com/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(libs.akanicore) // clone https://github.com/EinJojo/AkaniCore and run `./gradlew api:publishMavenLocal` so that this works
    compileOnly(libs.caffeine)
    compileOnly(libs.paper)
    compileOnly(libs.placeholderapi)
    implementation(libs.acf)
    implementation(libs.fastboard)
    annotationProcessor(libs.acf)
    implementation(kotlin("stdlib-jdk8"))
}

java {
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
kotlin {
    jvmToolchain(17)
}
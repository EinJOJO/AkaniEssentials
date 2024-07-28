plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
    kotlin("jvm")
}

group = "it.einjojo.akani"
version = "1.7.5"

repositories {
    mavenCentral()
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.oraxen.com/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://repo.akani.dev/releases")
    mavenLocal()

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
    implementation(libs.obliviateinvcore)
    implementation(libs.obliviateinvpagination)
    implementation(libs.obliviateinvadvancedslot)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
        relocate("mc.obliviate.inventory", "it.einjojo.akani.essentials.inventory")

    }

    test {
        useJUnitPlatform()
    }
}
kotlin {
    jvmToolchain(17)
}
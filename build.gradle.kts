import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "me.davipccunha.tests"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly(fileTree("libs") { include("*.jar") })
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly(fileTree("D:\\Local Minecraft Server\\plugins") { include("bukkit-utils.jar") })
    compileOnly(fileTree("D:\\Minecraft Dev\\artifacts\\") { include("economy-api.jar") })
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "11"
    targetCompatibility = "11"
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")
    archiveFileName.set("${project.name}.jar")

    destinationDirectory.set(file("D:\\Local Minecraft Server\\plugins"))
}

bukkit {
    name = project.name
    prefix = "Virtual Chest" // As shown in console
    apiVersion = "1.8"
    version = "${project.version}"
    main = "me.davipccunha.tests.virtualchest.VirtualChestPlugin"
    softDepend = listOf("economy")
    depend = listOf("bukkit-utils")
    description = "Plugin that allows players to buy and access virtual chests to store items."
    author = "Davi C"

    commands {
        register("bau") {
            aliases = listOf("baus", "chest", "chests")
            description = "Abre o menu principal dos baús virtuais"
        }
    }
}


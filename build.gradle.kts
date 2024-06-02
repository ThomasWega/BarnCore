plugins {
    `maven-publish`
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.3"
}

group = "com.bof.barn"
version = "1.0.0-SNAPSHOT"
description = "Core plugin for Barn gamemode"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.kryptonmc.org/releases") }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
    maven {
        name = "CodeMC"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

dependencies {
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
    api("com.bof:toolkit:1.0-SNAPSHOT")
    api("com.github.unldenis:Hologram-Lib:2.6.0")
    api("com.github.stefvanschie.inventoryframework:IF:0.10.11")
    api("cloud.commandframework", "cloud-paper", "1.8.4")
    api("de.tr7zw:item-nbt-api:2.12.0")
    implementation(platform("com.intellectualsites.bom:bom-newest:1.37"))
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.bof.barn:world-generator:1.0.0-SNAPSHOT")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }
    compileOnly("me.neznamy:tab-api:4.0.2")
    compileOnly("me.clip:placeholderapi:2.11.3")
    compileOnly ("com.comphenix.protocol:ProtocolLib:4.7.0")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    shadowJar {
        archiveFileName.set("BoFBarnCore-${project.version}.jar")
        relocate("de.tr7zw.changeme.nbtapi", "com.bof.barn.core")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("barn.core") {
            from(components["java"])
        }
    }
}
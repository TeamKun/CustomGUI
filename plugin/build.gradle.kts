import de.undercouch.gradle.tasks.download.Download

plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

val pluginName: String by extra
val pluginVersion: String by extra
val paperVersion: String by extra
val serverDirectory: String by extra
val paperUrl: String by extra

group = "kotx"
version = pluginVersion

repositories {
    mavenCentral()
    jcenter()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://kotlin.bintray.com/kotlinx/")
}

allprojects {
    repositories {
        maven("https://jitpack.io")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.koin:koin-core:2.2.2")
    implementation("org.koin:koin-core-ext:2.2.2")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "latest.release")
    implementation("ch.qos.logback", "logback-classic", "latest.release")
    implementation("com.destroystokyo.paper", "paper-api", paperVersion)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.1")
    implementation("org.apache.lucene:lucene-suggest:5.3.0")
    implementation("com.github.TeamKun:FlyLib-Reloaded:1.04")
}

tasks {
    compileJava {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    javadoc {
        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }

        from(sourceSets.main.get().resources.srcDirs) {
            filter {
                it.replace("@name@", pluginName)
                    .replace("@version@", pluginVersion)
            }
        }
    }

    shadowJar {
        archiveFileName.set("$pluginName v$pluginVersion.jar")
    }

    create<Copy>("buildPlugin") {
        from(shadowJar)
        var dest = file("$rootDir/server/plugins")
        if (File(dest, shadowJar.get().archiveFileName.get()).exists()) dest = File(dest, "update")
        into(dest)
    }

    create<DefaultTask>("setupWorkspace") {
        doLast {
            val paperDir = File(rootDir, "server")

            paperDir.mkdirs()

            val download by registering(Download::class) {
                src(paperUrl)
                dest(paperDir)
            }
            val paper = download.get().outputFiles.first()

            download.get().download()

            javaexec {
                workingDir(paperDir)
                main = "-jar"
                args("./${paper.name}", "nogui")
            }

            val eula = File(paperDir, "eula.txt")
            eula.writeText(eula.readText(Charsets.UTF_8).replace("eula=false", "eula=true"), Charsets.UTF_8)
            val serverProperties = File(paperDir, "server.properties")
            serverProperties.writeText(
                serverProperties.readText(Charsets.UTF_8)
                    .replace("online-mode=true", "online-mode=false")
                    .replace("difficulty=easy", "difficulty=peaceful")
                    .replace("spawn-protection=16", "spawn-protection=0")
                    .replace("gamemode=survival", "gamemode=creative")
                    .replace("level-name=world", "level-name=dev_world")
                    .replace("level-type=default", "level-type=flat")
                    .replace("motd=A Minecraft Server", "motd=Kotx Development Server")
                    .replace("max-tick-time=60000", "max-tick-time=-1")
                    .replace("view-distance=10", "view-distance=16"), Charsets.UTF_8
            )
            val runBat = File(paperDir, "run.bat")
            if (!runBat.exists()) {
                runBat.createNewFile()
                runBat.writeText("java -Xms3G -Xmx3G -jar ./${paper.name} nogui", Charsets.UTF_8)
            }
        }
    }
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
}

group = "kotx"
version = "0.1"

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
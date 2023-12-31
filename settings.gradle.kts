/*
 * This file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 * For more detailed information on multi-project builds, please refer to https://docs.gradle.org/8.4/userguide/building_swift_projects.html in the Gradle documentation.
 */

rootProject.name = "posts-dump-platform"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.10"
        id("com.google.devtools.ksp") version "1.9.10-1.0.13"
        id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
    }
}

include("posts-dump-backend")

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
}
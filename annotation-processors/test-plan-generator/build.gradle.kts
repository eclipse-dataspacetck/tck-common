/*
 *  Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */


plugins {
    `java-library`
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    }
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    implementation(project(":api:core-api"))
    implementation(libs.okhttp)
    implementation(libs.junit.jupiter)
    implementation(libs.junit.platform.engine)
    implementation(libs.markdown.gen)
    implementation(libs.plantuml)

    testImplementation(libs.assertj)
}

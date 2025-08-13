/*
 * Copyright 2023-2025 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:OptIn(InternalRpcApi::class)

import kotlinx.rpc.buf.tasks.BufGenerateTask
import kotlinx.rpc.internal.InternalRpcApi
import kotlinx.rpc.internal.configureLocalProtocGenDevelopmentDependency
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.conventions.kmp)
    alias(libs.plugins.kotlinx.rpc)
}

configureLocalProtocGenDevelopmentDependency(sourceSetSuffix = "Main")

val generatedCodeDir = layout.projectDirectory
    .dir("src")
    .dir("commonMain")
    .dir("generated-code")
    .asFile

tasks.withType<BufGenerateTask>().configureEach {
    if (name.contains("Main")) {
        includeWkt = true
        outputDirectory = generatedCodeDir
    }
}

kotlin {
    // for timestamp
    compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_2_1
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.protobuf.protobufCore)
                api(projects.grpc.grpcCodec)

                api(libs.kotlinx.io.core)
            }
        }
    }
}

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
    `maven-publish`
    signing
    checkstyle
    jacoco
    `jacoco-report-aggregation`
    alias(libs.plugins.nexuspublishing)
}

allprojects {

    apply(plugin = "java-library")
    apply(plugin = "checkstyle")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        withSourcesJar()
        withJavadocJar()
    }

    tasks.test {
        useJUnitPlatform()
        systemProperty("dataspacetck.launcher", "org.eclipse.dataspacetck.dsp.system.DspSystemLauncher")
    }

    tasks.jar {
        metaInf {
            from("${rootProject.projectDir.path}/LICENSE")
            from("${rootProject.projectDir.path}/DEPENDENCIES")
            from("${rootProject.projectDir.path}/NOTICE.md")
        }
    }

    dependencies {
        implementation(rootProject.libs.junit.jupiter)
        implementation(rootProject.libs.junit.platform.engine)
    }

    if (!project.hasProperty("skip.signing")) {
        apply(plugin = "signing")
        publishing {
            signing {
                useGpgCmd()
                sign(publishing.publications)
            }
        }
    }


}

subprojects {

    afterEvaluate {

        publishing {
            publications.forEach { i ->
                val mp = (i as MavenPublication)
                mp.pom {
                    name.set(project.name)
                    description.set("Compliance Verification Toolkit")
                    url.set("https://projects.eclipse.org/projects/technology.dataspacetck")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                        developers {
                            developer {
                                id.set("JimMarino")
                                name.set("Jim Marino")
                                email.set("jmarino@metaformsystems.com")
                            }
                            developer {
                                id.set("PaulLatzelsperger")
                                name.set("Paul Latzelsperger")
                                email.set("paul.latzelsperger@beardyinc.com")
                            }
                            developer {
                                id.set("EnricoRisa")
                                name.set("Enrico Risa")
                                email.set("enrico.risa@gmail.com")
                            }
                        }
                        scm {
                            connection.set("scm:git:git@github.com:eclipse-dataspacetck/cvf.git")
                            url.set("https://github.com/eclipse-dataspacetck/cvf.git")
                        }
                    }
                }
            }
        }

    }

    publishing {
        publications {
            if (project.subprojects.isEmpty()) {
                create<MavenPublication>(project.name) {
                    artifactId = project.name
                    from(components["java"])
                }
            }
        }
    }
}


// needed for running the dash tool
tasks.register("allDependencies", DependencyReportTask::class)

// disallow any errors
checkstyle {
    maxErrors = 0
}

nexusPublishing {
    repositories {
        sonatype {  //only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            username.set(System.getenv("CENTRAL_SONATYPE_TOKEN_USERNAME") ?: return@sonatype)
            password.set(System.getenv("CENTRAL_SONATYPE_TOKEN_PASSWORD") ?: return@sonatype)
        }
    }
}



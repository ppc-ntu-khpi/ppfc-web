val serializationVersion = properties["serialization_version"]
val koinVersion = properties["koin_version"]
val ktorVersion = properties["ktor_version"]
val pagingVersion = properties["paging_version"]

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.5.0"
    id("com.github.gmazzo.buildconfig") version "3.1.0"
}

group = "org.ppfc"
version = "0.1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        nodejs {
            version = "18.16.0"
        }

        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            buildConfig {
                buildConfigField(
                    "kotlin.String",
                    "API_BASE_ADDRESS",
                    "\"http://ppfc.us-east-2.elasticbeanstalk.com/api/\""
                )

                buildConfigField(
                    "kotlin.Long",
                    "LOG_LEVEL",
                    "6"
                )
            }

            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)

                // Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

                // Dependency injection
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation("io.insert-koin:koin-core-coroutines:$koinVersion")
                implementation("io.insert-koin:koin-compose:1.0.1")

                // Ktor client
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                // Paging
                implementation("app.cash.paging:paging-common:$pagingVersion")
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
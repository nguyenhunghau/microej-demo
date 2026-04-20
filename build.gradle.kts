/*
 * Kotlin
 */
plugins {
    id("com.microej.gradle.application") version "1.3.0"
}

group = "com.example"
version = "0.1.0-RC"

microej {
    applicationEntryPoint = "com.example.microej.Main"
    // Uncomment to use "prod" architecture when using a VEE Port (defaults to "eval")
//    architectureUsage = "prod"
}

dependencies {

    implementation("ej.api:edc:1.3.7")
    implementation("ej.api:bon:1.4.4")
    implementation("ej.api:device:1.2.0")
    implementation("ej.library.eclasspath:logging:1.2.1")

    // UI stack (align versions with the included `microej-ui` build)
    implementation("ej.api:microui:3.1.0")
    implementation("ej.api:drawing:1.0.2")
    implementation("ej.library.ui:widget:5.3.1")
    implementation("ej.library.runtime:basictool:1.5.0")
    implementation("ej.library.runtime:service:1.1.1")
    implementation("ej.library.eclasspath:collections:1.4.0")

    // Match NXP VEE port (libs.versions.toml) for real sockets on device
    implementation("ej.api:net:1.1.4")
    implementation("ej.api:ssl:2.2.3")

    // Provide AppStyle/Page from the included microej-ui build
//    implementation("com.microej.demo.showcase:microej-ui:1.0.0")

    //Uncomment the microejVee dependency to set the VEE Port or Kernel to use
    microejVee("com.nxp.vee.mimxrt1170:vee-port:3.1.0")

    // File system foundation (provides ej.io.* APIs)
    implementation("ej.api:fs:2.1.0")
    implementation("com.microej.pack:fs:6.0.4")
}
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            microej.useMicroejTestEngine(this)

            dependencies {
                implementation(project())
                implementation("ej.api:edc:1.3.7")
                implementation("ej.api:bon:1.4.4")
                implementation("ej.library.test:junit:1.12.0")
            }
        }
    }
}

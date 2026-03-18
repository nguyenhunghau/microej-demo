plugins {
	id("com.microej.gradle.application") version libs.versions.microej.sdk
}

group = "com.microej.demo.showcase"
version = "1.0.0"

microej {
	applicationEntryPoint = "com.microej.demo.showcase.DemoApp"
}

dependencies {
	implementation(libs.api.edc)
	implementation(libs.api.microui)
	implementation(libs.api.drawing)
	implementation(libs.api.hal)
	implementation(libs.api.device)
	implementation(libs.api.fs)
	implementation(libs.api.net)
	implementation(libs.library.widget)
	implementation(libs.library.basictool)
	implementation(libs.library.service)
	implementation(libs.library.collections)

	microejVee(libs.vee.port.st.stm32f7508)
}

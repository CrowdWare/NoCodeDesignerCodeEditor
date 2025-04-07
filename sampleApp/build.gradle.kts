import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
}

kotlin {
	applyDefaultHierarchyTemplate()
	jvm("desktop")

	wasmJs {
		moduleName = "sampleApp"
		browser {
			commonWebpackConfig {
				outputFileName = "sampleApp.js"
			}
		}
		binaries.executable()
	}

	sourceSets {

		val commonMain by getting {
			dependencies {
				implementation(projects.composeTextEditor)
				implementation(projects.composeTextEditorSpellCheck)
				implementation(compose.runtime)
				implementation(compose.foundation)
				implementation(compose.material3)
				implementation(compose.materialIconsExtended)
				implementation(compose.ui)
				implementation(compose.components.resources)
				implementation(compose.components.uiToolingPreview)
				implementation(compose.components.resources)
				implementation(libs.symspellkt)
				implementation(libs.symspellkt.fdic)
			}
		}

		val desktopMain by getting {
			dependencies {
				implementation(libs.kotlinx.coroutines.swing)

				implementation(libs.androidx.lifecycle.viewmodel)
				implementation(libs.androidx.lifecycle.runtime.compose)

				implementation(compose.desktop.currentOs)
				implementation(compose.runtime)
				implementation(compose.foundation)
				implementation(compose.material3)
				implementation(compose.materialIconsExtended)
				implementation(compose.ui)
				implementation(compose.components.resources)
				implementation(compose.components.uiToolingPreview)
			}
		}

		val desktopTest by getting {
			dependencies {
				implementation(libs.jetbrains.kotlin.test)
				implementation(libs.jetbrains.kotlin.test.junit)
				implementation(libs.mockk)
				implementation(libs.kotlinx.coroutines.test)
				implementation(libs.kotlinx.coroutines.test.jvm)
			}
		}
	}
}

compose.desktop {
	application {
		mainClass = "MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "com.darkrockstudios.texteditor"
			packageVersion = "1.0.0"
		}
	}
}

compose.experimental {
	web.application {}
}

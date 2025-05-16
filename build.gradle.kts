import com.android.builder.symbols.exportToCompiledJava

plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.dokka") version "1.9.10"
	id("maven-publish")
}

android {
	namespace = "dev.frozenmilk.sinister"
	compileSdk = 30

	defaultConfig {
		minSdk = 24
		//noinspection ExpiredTargetSdkVersion
		targetSdk = 28

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17

		kotlin {
			compilerOptions {
				freeCompilerArgs.add("-Xjvm-default=all")
			}
		}
	}
}

repositories {
	mavenCentral()
	google() // Needed for androidx
	maven {
		url = uri("https://repo.dairy.foundation/releases")
	}
}

dependencies {
	//noinspection GradleDependency
	implementation("androidx.appcompat:appcompat:1.2.0")
	testImplementation("junit:junit:4.13.2")

	implementation("dev.frozenmilk.dairy:Util:1.1.1")

	compileOnly("org.firstinspires.ftc:RobotCore:10.0.0")
	compileOnly("org.firstinspires.ftc:FtcCommon:10.0.0")

	testImplementation("org.firstinspires.ftc:FtcCommon:10.0.0")
}

publishing {
	repositories {
		maven {
			name = "Dairy"
			url = uri("https://repo.dairy.foundation/releases")
			credentials(PasswordCredentials::class)
			authentication {
				create<BasicAuthentication>("basic")
			}
		}
	}
	publications {
		register<MavenPublication>("release") {
			groupId = "dev.frozenmilk"
			artifactId = "Sinister-local"
			version = "1.0.0-local"

			afterEvaluate {
				from(components["release"])
			}
		}
	}
}

import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import java.text.SimpleDateFormat
import java.util.Date

val modId: String by project
val modName: String by project
val modAuthor: String by project
val modVersion: String by project
val minecraftVersion: String by project
val mixinVersion: String by project

val modNameStripped = modName.replace(" ", "")
val jarVersion = "$minecraftVersion+v$modVersion"

buildscript {
	repositories {
		maven("https://repo.spongepowered.org/maven")
	}
}

plugins {
	`java-library`
	idea
	id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

idea {
	module {
		excludeDirs.add(project.file("run"))
	}
}

repositories {
	maven("https://repo.spongepowered.org/maven")
	mavenCentral()
}

dependencies {
	implementation("org.spongepowered:mixin:$mixinVersion")
	api("com.google.code.findbugs:jsr305:3.0.2")
}

base {
	archivesName.set("$modNameStripped-Common")
}

minecraft {
	version(minecraftVersion)
	runs.clear()
}

allprojects {
	group = "com.$modAuthor.$modId"
	version = modVersion
	
	apply(plugin = "java")
	
	dependencies {
		implementation("org.jetbrains:annotations:22.0.0")
	}
	
	extensions.getByType<JavaPluginExtension>().apply {
		toolchain.languageVersion.set(JavaLanguageVersion.of(16))
	}
	
	tasks.withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release.set(16)
	}
}

subprojects {
	repositories {
		maven("https://repo.spongepowered.org/maven")
	}
	
	dependencies {
		implementation(rootProject)
	}
	
	base {
		archivesName.set("$modNameStripped-${project.name}")
	}
	
	tasks.withType<JavaCompile> {
		source({ rootProject.sourceSets.main.get().allSource })
	}
	
	tasks.processResources {
		from(rootProject.sourceSets.main.get().resources)
		
		inputs.property("name", modName)
		inputs.property("version", modVersion)
	}
	
	tasks.jar {
		archiveVersion.set(jarVersion)
		
		from(rootProject.file("LICENSE"))
		
		manifest {
			attributes(
				"Specification-Title" to modId,
				"Specification-Vendor" to modAuthor,
				"Specification-Version" to "1",
				"Implementation-Title" to "$modNameStripped-${project.name}",
				"Implementation-Vendor" to modAuthor,
				"Implementation-Version" to modVersion,
				"Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
				"MixinConfigs" to "$modId.mixins.json"
			)
		}
	}
}

val copyJars = tasks.register<Copy>("copyJars") {
	duplicatesStrategy = EXCLUDE
	
	for (subproject in subprojects) {
		dependsOn(subproject.tasks.build)
		from(subproject.base.libsDirectory.file("${subproject.base.archivesName.get()}-$jarVersion.jar"))
	}
	
	into(file("${project.buildDir}/dist"))
}

tasks.build {
	finalizedBy(copyJars)
}

@file:Suppress("ConvertLambdaToReference")

import org.gradle.api.file.DuplicatesStrategy.EXCLUDE

val modId: String by project
val modName: String by project
val modDescription: String by project
val modAuthor: String by project
val modVersion: String by project
val modLicense: String by project
val modSourcesURL: String by project
val modIssuesURL: String by project

val minecraftVersion: String by project
val mixinVersion: String by project

val minimumMinecraftVersion: String by project
val minimumNeoForgeVersion: String by project
val minimumFabricVersion: String by project

val modNameStripped = modName.replace(" ", "")
val jarVersion = "$minecraftVersion+v$modVersion"

plugins {
	idea
	`java-library`
	id("fabric-loom")
}

idea {
	module {
		excludeDirs.add(file("gradle"))
		excludeDirs.add(file("run"))
		
		if (findProject(":NeoForge") == null) {
			excludeDirs.add(file("NeoForge"))
		}
		
		if (findProject(":Fabric") == null) {
			excludeDirs.add(file("Fabric"))
		}
	}
}

repositories {
	mavenCentral()
}

base {
	archivesName.set("$modNameStripped-Common")
}

loom {
	runs {
		named("client") {
			ideConfigGenerated(false)
		}
		
		findByName("server")?.let(::remove)
	}
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	mappings(loom.officialMojangMappings())
	
	compileOnly("net.fabricmc:sponge-mixin:$mixinVersion")
	api("com.google.code.findbugs:jsr305:3.0.2")
}

allprojects {
	group = "com.$modAuthor.$modId"
	version = modVersion
	
	apply(plugin = "java-library")
	
	extensions.getByType<JavaPluginExtension>().apply {
		toolchain.languageVersion.set(JavaLanguageVersion.of(21))
	}
	
	tasks.withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release.set(21)
	}
	
	val runJvmArgs = mutableSetOf<String>().also {
		extra["runJvmArgs"] = it
	}
	
	if (project.javaToolchains.launcherFor(java.toolchain).map { it.metadata.vendor }.orNull == "JetBrains") {
		runJvmArgs.add("-XX:+AllowEnhancedClassRedefinition")
	}
	
	dependencies {
		implementation("org.jetbrains:annotations:24.1.0")
	}
}

subprojects {
	dependencies {
		implementation(project(rootProject.path, configuration = "namedElements"))
	}
	
	base {
		archivesName.set("$modNameStripped-${project.name}")
	}
	
	listOf("compileJava", "compileTestJava").forEach {
		tasks.named<JavaCompile>(it) {
			source({ rootProject.sourceSets.main.get().allSource })
		}
	}
	
	tasks.processResources {
		inputs.property("id", modId)
		inputs.property("name", modName)
		inputs.property("description", modDescription)
		inputs.property("version", modVersion)
		inputs.property("author", modAuthor)
		inputs.property("license", modLicense)
		inputs.property("sourcesURL", modSourcesURL)
		inputs.property("issuesURL", modIssuesURL)
		inputs.property("minimumMinecraftVersion", minimumMinecraftVersion)
		inputs.property("minimumNeoForgeVersion", minimumNeoForgeVersion)
		inputs.property("minimumFabricVersion", minimumFabricVersion)
		
		from(rootProject.sourceSets.main.get().resources) {
			expand(inputs.properties)
		}
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
			)
		}
	}
	
	tasks.test {
		onlyIf { false }
	}
}

val copyJars = tasks.register<Copy>("copyJars") {
	group = "build"
	duplicatesStrategy = EXCLUDE
	
	for (subproject in subprojects) {
		dependsOn(subproject.tasks.assemble)
		from(subproject.base.libsDirectory.file("${subproject.base.archivesName.get()}-$jarVersion.jar"))
	}
	
	into(project.layout.buildDirectory.dir("dist"))
}

tasks.assemble {
	finalizedBy(copyJars)
}

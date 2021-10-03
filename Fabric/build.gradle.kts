plugins {
	idea
	id("fabric-loom") version "0.9-SNAPSHOT"
}

val minecraftVersion: String by project
val fabricVersion: String by project

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	modImplementation("net.fabricmc:fabric-loader:$fabricVersion")
	mappings(loom.officialMojangMappings())
}

loom {
	runs {
		named("client") {
			configName = "Fabric Client"
			client()
			runDir("run")
			ideConfigGenerated(true)
		}
		
		named("server") {
			configName = "Fabric Server"
			server()
			runDir("run")
			ideConfigGenerated(true)
		}
	}
}

tasks.processResources {
	filesMatching("fabric.mod.json") {
		expand(inputs.properties)
	}
}

tasks.jar {
	exclude("io/github/prospector/")
}

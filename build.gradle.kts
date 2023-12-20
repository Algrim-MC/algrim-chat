import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.3-SNAPSHOT"
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.9.21"
}

version = project.properties["mod_version"]!!
group = project.properties["maven_group"]!!

base {
    archivesName = project.properties["archives_base_name"].toString()
}

repositories {
    maven { url = uri("https://masa.dy.fi/maven") }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${project.properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

    modImplementation("net.fabricmc:fabric-language-kotlin:${project.properties["fabric_kotlin_version"]}")
    modImplementation("fi.dy.masa.malilib:malilib-fabric-${project.properties["minecraft_version"]}:${project.properties["malilib_version"]}")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.processResources {
    inputs.property("mod_id", project.properties["mod_id"])
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(
            "mod_id" to project.properties["mod_id"],
            "version" to project.version
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 17
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
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
    modImplementation("fi.dy.masa.malilib:malilib-fabric-1.20.6:${project.properties["malilib_version"]}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")

    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
    options.release = 21
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "21"
    }
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

tasks.wrapper {
    gradleVersion = "8.7"
    distributionType = Wrapper.DistributionType.ALL
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {}
}

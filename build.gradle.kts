plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"

    id("net.mamoe.mirai-console") version "2.15.0"
    id("me.him188.maven-central-publish") version "1.0.0-dev-3"
}

group = "xyz.cssxsh.mirai"
version = "1.0.7"

repositories {
    mavenLocal()
    mavenCentral()
}

mavenCentralPublish {
    useCentralS01()
    singleDevGithubProject("cssxsh", "mirai-authenticator")
    licenseFromGitHubProject("AGPL-3.0")
    workingDir = System.getenv("PUBLICATION_TEMP")?.let { file(it).resolve(projectName) }
        ?: buildDir.resolve("publishing-tmp")
    publication {
        artifact(tasks["buildPlugin"])
    }
}

dependencies {
    compileOnly("xyz.cssxsh.mirai:mirai-administrator:1.4.0")
    compileOnly("xyz.cssxsh.mirai:mirai-script-plugin:1.1.0")
    testImplementation(kotlin("test"))
    testImplementation("org.luaj:luaj-jse:3.0.1")
    testImplementation("org.jline:jline:3.23.0")
    //
    implementation(platform("net.mamoe:mirai-bom:2.14.0"))
    compileOnly("net.mamoe:mirai-console-compiler-common")
    testImplementation("net.mamoe:mirai-core-mock")
    testImplementation("net.mamoe:mirai-logging-slf4j")
    //
    implementation(platform("io.ktor:ktor-bom:2.2.4"))
    implementation("io.ktor:ktor-client-okhttp")
    implementation("io.ktor:ktor-client-encoding")
    //
    implementation(platform("org.slf4j:slf4j-parent:2.0.7"))
    testImplementation("org.slf4j:slf4j-simple")
}

mirai {
    jvmTarget = JavaVersion.VERSION_11
}

kotlin {
    explicitApi()
}

tasks {
    test {
        useJUnitPlatform()
    }
}

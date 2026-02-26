plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.mberk_yilmaz.capnp"
version = "1.0.0"

repositories {
    mavenCentral()
}

intellij {
    version.set("2024.3.2")
    type.set("IC") // IntelliJ IDEA Community Edition
}

grammarKit {
    // Version of Grammar-Kit to use
    jflexRelease.set("1.9.1")
    grammarKitRelease.set("2022.3.1")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    generateLexer {
        sourceFile.set(file("src/main/grammars/CapnProto.flex"))
        targetOutputDir.set(file("src/main/gen/com/mberk_yilmaz/capnp/lexer"))
        purgeOldFiles.set(true)
    }

    generateParser {
        sourceFile.set(file("src/main/grammars/CapnProto.bnf"))
        targetRootOutputDir.set(file("src/main/gen"))
        pathToParser.set("/com/mberk_yilmaz/capnp/parser/CapnProtoParser.java")
        pathToPsiRoot.set("/com/mberk_yilmaz/capnp/psi")
        purgeOldFiles.set(true)
    }

    compileKotlin {
        dependsOn(generateLexer, generateParser)
    }

    withType<JavaCompile> {
        dependsOn(generateLexer, generateParser)
    }

    patchPluginXml {
        sinceBuild.set("243")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/java", "src/main/gen")
    }
}

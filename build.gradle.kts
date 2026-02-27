plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.3"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.mberk_yilmaz.capnp"
version = "1.0.1"

repositories {
    mavenCentral()
}

intellij {
    version.set("2025.1")
    type.set("IU") // IntelliJ IDEA Ultimate (compatible with CLion, PyCharm, etc)
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

    named("buildSearchableOptions") {
        enabled = false
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

    withType<JavaCompile> {
        dependsOn(generateLexer, generateParser)
    }

    patchPluginXml {
        sinceBuild.set("251")
        untilBuild.set("253.*")
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

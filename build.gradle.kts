val kotlinTelegramBotVersion by extra { "5.0.0" }
val exposedVersion by extra { "0.24.1" }

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"

    application
}

repositories {
    jcenter()
    maven("https://jitpack.io")
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:$kotlinTelegramBotVersion")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClassName = "dev.knonm.catm.AppKt"
}

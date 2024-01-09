import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.21"
	kotlin("plugin.spring") version "1.9.21"
	kotlin("plugin.jpa") version "1.9.21"
}

group = "com.mvp.order"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

val springVersion by extra { "3.2.1" }

configurations.all {
	exclude(group = "commons-logging", module = "commons-logging")
}

dependencies {

	//Spring
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// PostgresSQL
	runtimeOnly("org.postgresql:postgresql")

	// Kotlin utils
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.0")

	//SQS and SNS
	implementation("software.amazon.awssdk:sqs:2.22.5")
	implementation("software.amazon.awssdk:sns:2.22.5")

	// Mercado Pago SDK
	implementation("com.mercadopago:sdk-java:2.1.14")

	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("io.mockk:mockk:1.13.8")
	testImplementation("com.h2database:h2:2.2.224")
	testImplementation("io.rest-assured:rest-assured:5.4.0")
	implementation("io.rest-assured:json-schema-validator:5.4.0")

	//Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Cucumber
	implementation("io.cucumber:cucumber-spring:7.15.0")
	implementation("io.cucumber:cucumber-java:7.15.0")
	implementation("io.cucumber:cucumber-junit:7.15.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	enabled = true
	useJUnitPlatform(
	)
}
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit

plugins {
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	//kotlin("plugin.jpa") version "1.9.23"
	id("org.jetbrains.kotlinx.kover") version "0.8.0" // code coverage
	id("io.gitlab.arturbosch.detekt") version "1.23.6" // static analyse
}

group = "com.example"
version = "2.0.0"

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2023.0.3"

val openApiVersion by extra("2.6.0")
val kLoggingVersion by extra("6.0.8")
val preliquibaseVersion by extra("1.5.1")
val jwtVersion by extra("0.12.6")
val mockitoKotlinVersion by extra("5.3.1")

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// cloud
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

	// reactive data base
	runtimeOnly("org.postgresql:r2dbc-postgresql")
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

	// liquibase
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.liquibase:liquibase-core")
	implementation("org.springframework:spring-jdbc")
	implementation ("net.lbruun.springboot:preliquibase-spring-boot-starter:$preliquibaseVersion")

	// log
	implementation("io.github.oshai:kotlin-logging-jvm:$kLoggingVersion")

	// security
	implementation("org.springframework.boot:spring-boot-starter-security")

	// jwt
	implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
	implementation("io.jsonwebtoken:jjwt-impl:$jwtVersion")
	implementation("io.jsonwebtoken:jjwt-jackson:$jwtVersion")

	// open api
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$openApiVersion")

	// kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	// test containers
	testImplementation("org.testcontainers:r2dbc")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
	testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

detekt {
	config.setFrom("$projectDir/../config/detekt/detekt.yml")
}

kover {
	reports {
		total {
			html {
				onCheck = true
			}
		}
		filters {
			excludes {
				classes(
					"*configurations*",
					"*utils*",
					"*AfaloanApplication*"
				)
			}
		}
		verify {
			rule("Basic Line Coverage") {
				bound {
					minValue = 70
					coverageUnits = CoverageUnit.LINE
					aggregationForGroup = AggregationType.COVERED_PERCENTAGE
				}
			}

			rule("Branch Coverage") {
				bound {
					minValue = 80
					coverageUnits = CoverageUnit.BRANCH
					aggregationForGroup = AggregationType.COVERED_PERCENTAGE
				}
			}
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = JavaVersion.VERSION_21.toString()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

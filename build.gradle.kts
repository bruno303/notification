import com.bso.notification.tasks.CustomSpringBootRunTask

plugins {
	java
	id("org.springframework.boot") version "3.1.1"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.bso"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

object Version {
	const val AWS_SDK = "2.20.56"
	const val MICROMETER_PROMETHEUS = "1.11.2"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// AWS
	implementation(platform("software.amazon.awssdk:bom:${Version.AWS_SDK}"))
	implementation("software.amazon.awssdk:sqs")

	// Monitoring
	implementation("io.micrometer:micrometer-registry-prometheus:${Version.MICROMETER_PROMETHEUS}")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.opentelemetry:opentelemetry-exporter-zipkin")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register("customBootRun", CustomSpringBootRunTask::class) {
	group = "application"
	mainClass.set("com.bso.notification.NotificationApplication")
	classpath = project.sourceSets["main"].runtimeClasspath
}

tasks.bootRun {
	jvmArgs = listOf("-Dspring.profiles.active=local")
}
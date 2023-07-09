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
	const val awsSdk = "2.20.56"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation(platform("software.amazon.awssdk:bom:${Version.awsSdk}"))
	implementation("software.amazon.awssdk:sqs")

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
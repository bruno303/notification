## Notification

Project to send notifications (webhooks) to previously registered clients.

## Technologies

- Java 17
- Gradle
- Spring Webflux
- MongoDB
- AWS
- Localstack (locally)

## Program Flow
![img.png](docs%2Fimg%2Fimg.png)

## CustomBootRun gradle task
This task was created to allow pass jvm arguments to JavaExec task using command line.

JvmArguments must be separared by space.

Task definition is in `build.gradle.kts`. Task souce code is in `buildSrc/src/main/java/com/bso/notification/tasks/CustomSpringBootRunTask.java`.

With this task is possible to run the program like this:

```shell
./gradlew customBootRun --customJvmArgs="<JVM ARGUMENTS HERE>"
```

Example:
```shell
./gradlew customBootRun --customJvmArgs="-Dspring.output.ansi.enabled=ALWAYS -Dspring.profiles.active=local"
```
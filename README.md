# RevCRM

RevCRM is an open source (Apache 2.0 Licensed) CRM platform.

* Enterprise-grade Hibernate ORM
* Rock-solid JVM-based backend written in Kotlin
* Modern, responsive UI using React and TypeScript
* Modular architecture. Only install the features you need

This project is still very much a work-in-progress but watch this space :)

## Developer Notes

### Building and Running Locally

```
./gradlew run
```

### Build docker image

```
./gradlew build
docker build -t revcrm:latest .
```

#### Test-running the docker image

```
docker run --rm -it --env-file=revcrm.env -p 8800:8800 revcrm:latest
```
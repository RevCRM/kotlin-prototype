# RevCRM

RevCRM is an open source (Apache 2.0 Licensed) CRM platform.

* Rock-solid JVM-based backend written in Kotlin
* Easily define Entities and relationships using POJOs + Annotations
* Auto-generated GraphQL API
* Entity Validation using JSR 380 Standard Annotations
* Modern, responsive UI using React and TypeScript
* Modular architecture. Only install the features the customer needs

This project is still very much a work-in-progress but watch this space :)

## Developer Notes

### Linting

We use [ktlint](https://ktlint.github.io/) to enforce our Kotlin coding style. We recommend installing and using the
git commit hook (see the ktlint Usage instructions) to make sure your code matches the recommended style.

For the front end we use [tslint](https://palantir.github.io/tslint/), which will be installed when you do an
`npm install` and runs as part of the build.

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

### Developing the Client

The revcrm-client is automatically built as part of the gradle build, but it is also a self-contained NodeJS project
which can be worked on seperately.

Please note that you'll need at least **NodeJS v10.x** installed to build & test the client.
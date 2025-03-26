# DSAG Demo Application

Minimal showcase for [SAP Cloud SDK for AI for Java](https://github.com/SAP/ai-sdk-java)

## Features

* Swagger UI
* REST endpoints for chat completion
  * [simple](http://localhost:8080/orchestration/simple)
  * [template](http://localhost:8080/orchestration/template)
  * [filtering](http://localhost:8080/orchestration/filtering)
  * [stream](http://localhost:8080/orchestration/stream) (Swagger UI not recommmended)

## Prerequisites

* JRE 17+
* Maven
* Service Key (JSON) provided as environment variable `AICORE_SERVICE_KEY` or `.env` file containing one line `AICORE_SERVICE_KEY={...}`

## Usage


Start `Application.java` in IDE or use Maven CLI:

```
mvn clean install
mvn spring-boot:run
```

## Screenshot

![image](https://github.com/user-attachments/assets/6ec1eb2d-f651-45d8-8a9f-3bb3762124f0)

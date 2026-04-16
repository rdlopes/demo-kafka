# Project: demo-kafka
This project is a Spring Boot application designed to receive events as Avro objects over Kafka.

## Tech Stack
- Java 25
- Spring Boot 4
- Spring for Apache Kafka
- Apache Avro
- Confluent Schema Registry

## Development Guidelines
- Use Avro schemas (`.avsc`) for event definitions.
- Place schemas in `src/main/resources/avro`.
- Ensure `avro-maven-plugin` is used for code generation.
- Use `io.confluent.kafka.serializers.KafkaAvroSerializer` and `io.confluent.kafka.serializers.KafkaAvroDeserializer`.
- Use `@KafkaListener` for receiving events.
- Configure `spring.kafka.consumer.properties.specific.avro.reader=true` to use the generated classes.
- Follow standard Spring Boot application patterns (service layer, configuration, etc.).
- Maintain unit and integration tests using `spring-boot-starter-kafka-test`.

## Project Structure
- `src/main/resources/avro`: Avro schema files.
- `src/main/java/io/github/rdlopes/kafka`: Main application package.

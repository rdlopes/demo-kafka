package io.github.rdlopes.kafka;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@EmbeddedKafka(topics = {"user-events", "account-events"})
@CucumberContextConfiguration
@SpringBootTest(classes = DemoKafkaApplication.class)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
public class CucumberSpringConfiguration {
}

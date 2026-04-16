package io.github.rdlopes.kafka.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import io.github.rdlopes.kafka.avro.AccountEvent;
import io.github.rdlopes.kafka.avro.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Map;

public class KafkaSteps {

  private static final String USER_EVENTS_TOPIC = "user-events";
  private static final String ACCOUNT_EVENTS_TOPIC = "account-events";

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public KafkaSteps(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @When("a user event is sent for id {string} with name {string} and email {string}")
  public void a_user_event_is_sent(String id, String name, String email) {
    sendUserEvent(id, name, email);
  }

  @When("the following user events are sent:")
  public void the_following_user_events_are_sent(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      sendUserEvent(row.get("id"), row.get("name"), row.get("email"));
    }
  }

  @When("an account event is sent for id {string} for user {string}")
  public void an_account_event_is_sent(String id, String userId) {
    sendAccountEvent(id, userId);
  }

  @When("the following account events are sent:")
  public void the_following_account_events_are_sent(DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> row : rows) {
      sendAccountEvent(row.get("id"), row.get("userId"));
    }
  }

  private void sendUserEvent(String id, String name, String email) {
    UserEvent event = UserEvent.newBuilder()
                               .setId(id)
                               .setName(name)
                               .setEmail(email)
                               .setTimestamp(System.currentTimeMillis())
                               .build();
    kafkaTemplate.executeInTransaction(operations -> {
      try {
        return operations.send(USER_EVENTS_TOPIC, id, event).get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void sendAccountEvent(String id, String userId) {
    AccountEvent event = AccountEvent.newBuilder()
                                     .setId(id)
                                     .setUserId(userId)
                                     .setTimestamp(System.currentTimeMillis())
                                     .build();
    kafkaTemplate.executeInTransaction(operations -> {
      try {
        return operations.send(ACCOUNT_EVENTS_TOPIC, id, event).get();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
}

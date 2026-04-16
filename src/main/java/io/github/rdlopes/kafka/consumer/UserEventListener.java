package io.github.rdlopes.kafka.consumer;

import io.github.rdlopes.kafka.avro.UserEvent;
import io.github.rdlopes.kafka.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

  private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);

  private final UserService userService;

  public UserEventListener(UserService userService) {
    this.userService = userService;
  }

  @RetryableTopic(
    attempts = "3",
    backOff = @BackOff(delay = 2000, multiplier = 2.0),
    topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
  @KafkaListener(topics = "user-events")
  public void onUserEvent(UserEvent event) {
    log.info("Received UserEvent: {}", event);
    userService.processUserEvent(event);
  }

  @DltHandler
  public void handleDlt(UserEvent event) {
    log.error("UserEvent sent to DLT: {}", event);
  }
}

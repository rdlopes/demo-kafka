package io.github.rdlopes.kafka.consumer;

import io.github.rdlopes.kafka.avro.AccountEvent;
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
public class AccountEventListener {

  private static final Logger log = LoggerFactory.getLogger(AccountEventListener.class);

  private final UserService userService;

  public AccountEventListener(UserService userService) {
    this.userService = userService;
  }

  @RetryableTopic(
    attempts = "3",
    backOff = @BackOff(delay = 2000, multiplier = 2.0),
    topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE)
  @KafkaListener(topics = "account-events")
  public void onAccountEvent(AccountEvent event) {
    log.info("Received AccountEvent: {}", event);
    userService.processAccountEvent(event);
  }

  @DltHandler
  public void handleDlt(AccountEvent event) {
    log.error("AccountEvent sent to DLT: {}", event);
  }
}

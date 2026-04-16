package io.github.rdlopes.kafka.steps;

import io.cucumber.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationSteps {

  private static final Logger log = LoggerFactory.getLogger(ApplicationSteps.class);

  @Before
  public void beforeScenario() {
    log.info("Starting Cucumber scenario...");
  }
}

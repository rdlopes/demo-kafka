package io.github.rdlopes.kafka.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.rdlopes.kafka.persistence.AccountRepository;
import io.github.rdlopes.kafka.persistence.UserRepository;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class DatabaseSteps {

  private final UserRepository userRepository;

  private final AccountRepository accountRepository;

  public DatabaseSteps(UserRepository userRepository, AccountRepository accountRepository) {
    this.userRepository = userRepository;
    this.accountRepository = accountRepository;
  }

  @Before
  @Given("the database is empty")
  public void the_database_is_empty() {
    accountRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Then("there should be {int} users in the database")
  public void there_should_be_users_in_the_database(int count) {
    assertThat(userRepository.count()).isEqualTo(count);
  }

  @Then("there should be {int} accounts in the database")
  public void there_should_be_accounts_in_the_database(int count) {
    assertThat(accountRepository.count()).isEqualTo(count);
  }

  @Then("the user {string} should eventually exist with name {string} and email {string}")
  public void the_user_should_eventually_exist(String id, String name, String email) {
    await().atMost(5, TimeUnit.SECONDS)
           .untilAsserted(() -> {
             var userOpt = userRepository.findById(id);
             assertThat(userOpt).isPresent();
             var user = userOpt.get();
             assertThat(user.getName()).isEqualTo(name);
             assertThat(user.getEmail()).isEqualTo(email);
           });
  }

  @Then("the account {string} for user {string} should eventually exist")
  public void the_account_should_eventually_exist(String id, String userId) {
    await().atMost(5, TimeUnit.SECONDS)
           .untilAsserted(() -> {
             var account = accountRepository.findById(id);
             assertThat(account).isPresent();
             assertThat(account.get()
                               .getUser()
                               .getId()).isEqualTo(userId);
           });
  }
}

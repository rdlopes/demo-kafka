package io.github.rdlopes.kafka.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.rdlopes.kafka.persistence.AccountEntity;
import io.github.rdlopes.kafka.persistence.AccountRepository;
import io.github.rdlopes.kafka.persistence.UserEntity;
import io.github.rdlopes.kafka.persistence.UserRepository;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class RestSteps {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final RestTestClient restTestClient;

    private RestTestClient.ResponseSpec latestResponse;

    public RestSteps(UserRepository userRepository, AccountRepository accountRepository, RestTestClient restTestClient) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.restTestClient = restTestClient;
    }

    @Given("a user exists with id {string}, name {string}, email {string}")
    public void a_user_exists(String id, String name, String email) {
        userRepository.save(new UserEntity(id, name, email, System.currentTimeMillis()));
    }

    @Given("an account exists with id {string} for user {string}")
    public void an_account_exists(String id, String userId) {
        var user = userRepository.findById(userId).orElseThrow();
        accountRepository.save(new AccountEntity(id, user, System.currentTimeMillis()));
    }

    @When("I query the REST endpoint {string}")
    public void i_query_the_rest_endpoint(String endpoint) {
        latestResponse = restTestClient.get().uri(endpoint).exchange();
    }

    @Then("the REST endpoint {string} should return an empty list")
    public void the_rest_endpoint_should_return_an_empty_list(String endpoint) {
        restTestClient.get().uri(endpoint)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("[]");
    }

    @Then("the response should contain user {string} with {int} account")
    @Then("the response should contain user {string} with {int} accounts")
    public void the_response_should_contain_user_with_accounts(String userId, int accountCount) {
        latestResponse.expectStatus().isOk()
                .expectBody()
                .jsonPath("$[?(@.id == '" + userId + "')].accounts[0]")
                .value(List.class, accounts -> assertThat(accounts).hasSize(accountCount));
    }

    @Then("the response should be user {string} with {int} accounts")
    public void the_response_should_be_user_with_accounts(String userId, int accountCount) {
        latestResponse.expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(userId)
                .jsonPath("$.accounts").value(List.class, accounts -> assertThat(accounts).hasSize(accountCount));
    }
}

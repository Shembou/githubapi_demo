package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.GitHubRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnNonForkRepositoriesWithBranches() {
        String username = "Shembou";
        String url = "http://localhost:" + port + "/api/v1/github/" + username + "/repositories";

        ResponseEntity<GitHubRepository[]> response = restTemplate.getForEntity(url, GitHubRepository[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        GitHubRepository[] repositories = response.getBody();
        assertThat(repositories).isNotEmpty();

        for (GitHubRepository repo : repositories) {
            assertThat(repo.getOwnerLogin()).isEqualTo(username);
            assertThat(repo.getBranches()).isNotEmpty();
            repo.getBranches().forEach(branch -> {
                assertThat(branch.getName()).isNotBlank();
                assertThat(branch.getLastCommitSha()).isNotBlank();
            });
        }
    }
}

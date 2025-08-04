package com.example.demo.client;

import com.example.demo.dto.GitHubBranchResponse;
import com.example.demo.dto.GitHubRepositoryResponse;
import com.example.demo.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor

public class GitHubClient {

    @Value("${GITHUB_TOKEN:}")
    private String githubToken;

    private final RestClient restClient;

    public List<GitHubRepositoryResponse> getUserRepositories(String username) {
        try {
            return restClient.get()
                    .uri("https://api.github.com/users/{username}/repos", username)
                    .headers(headers -> headers.addAll(createHeaders()))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<GitHubRepositoryResponse>>() {
                    });
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<GitHubBranchResponse> getRepositoryBranches(String owner, String repo) {
        return restClient.get()
                .uri("https://api.github.com/repos/{owner}/{repo}/branches", owner, repo)
                .headers(headers -> headers.addAll(createHeaders()))
                .retrieve()
                .body(new ParameterizedTypeReference<List<GitHubBranchResponse>>() {
                });
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (githubToken != null && !githubToken.isEmpty()) {
            headers.setBearerAuth(githubToken);
        }
        headers.set("Accept", "application/vnd.github.v3+json");
        return headers;
    }
}
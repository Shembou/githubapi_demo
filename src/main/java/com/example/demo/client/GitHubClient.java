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
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor

public class GitHubClient {

    @Value("${GITHUB_TOKEN:}")
    private String githubToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<GitHubRepositoryResponse> getUserRepositories(String username) {
        try {
            ResponseEntity<List<GitHubRepositoryResponse>> response = restTemplate.exchange(
                    "https://api.github.com/users/" + username + "/repos",
                    HttpMethod.GET,
                    new HttpEntity<>(createHeaders()),
                    new ParameterizedTypeReference<List<GitHubRepositoryResponse>>() {
                    });
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<GitHubBranchResponse> getRepositoryBranches(String owner, String repo) {
        ResponseEntity<List<GitHubBranchResponse>> response = restTemplate.exchange(
                "https://api.github.com/repos/" + owner + "/" + repo + "/branches",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                new ParameterizedTypeReference<List<GitHubBranchResponse>>() {
                });
        return response.getBody();
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
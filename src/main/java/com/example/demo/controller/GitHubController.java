package com.example.demo.controller;

import com.example.demo.model.GitHubRepository;
import com.example.demo.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("/{username}/repositories")
    public List<GitHubRepository> getRepositories(@PathVariable String username) {
        return gitHubService.getNonForkRepositoriesWithBranches(username);
    }
}

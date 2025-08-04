package com.example.demo.service;

import com.example.demo.client.GitHubClient;
import com.example.demo.dto.GitHubBranchResponse;
import com.example.demo.dto.GitHubRepositoryResponse;
import com.example.demo.model.GitHubBranch;
import com.example.demo.model.GitHubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubClient gitHubClient;

    public List<GitHubRepository> getNonForkRepositoriesWithBranches(String username) {
        List<GitHubRepositoryResponse> repositories = gitHubClient.getUserRepositories(username);

        List<GitHubRepository> result = new ArrayList<>();

        for (GitHubRepositoryResponse repo : repositories) {
            if (!repo.isFork()) {
                List<GitHubBranchResponse> branches = gitHubClient.getRepositoryBranches(repo.getOwner().getLogin(),
                        repo.getName());

                List<GitHubBranch> branchList = branches.stream().map(branch -> {
                    GitHubBranch gitHubBranch = new GitHubBranch();
                    gitHubBranch.setName(branch.getName());
                    gitHubBranch.setLastCommitSha(branch.getCommit().getSha());
                    return gitHubBranch;
                }).collect(Collectors.toList());

                GitHubRepository gitHubRepository = new GitHubRepository();
                gitHubRepository.setName(repo.getName());
                gitHubRepository.setOwnerLogin(repo.getOwner().getLogin());
                gitHubRepository.setBranches(branchList);

                result.add(gitHubRepository);
            }
        }

        return result;
    }

}
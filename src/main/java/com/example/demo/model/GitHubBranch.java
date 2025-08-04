package com.example.demo.model;


import lombok.Data;

@Data
public class GitHubBranch {
    private String name;
    private String lastCommitSha;
}

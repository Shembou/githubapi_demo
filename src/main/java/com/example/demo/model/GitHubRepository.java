package com.example.demo.model;

import lombok.Data;
import java.util.List;

@Data
public class GitHubRepository {
    private String name;
    private String ownerLogin;
    private List<GitHubBranch> branches;
}

package com.example.demo;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.controller.GitHubController;
import com.example.demo.model.GitHubBranch;
import com.example.demo.model.GitHubRepository;
import com.example.demo.service.GitHubService;

@WebMvcTest(GitHubController.class)
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitHubService gitHubService;

    @Test
    public void shouldReturnNonForkRepositoriesWithBranches() throws Exception {
        String username = "Shembou";

        GitHubBranch branch = new GitHubBranch();
        branch.setName("main");
        branch.setLastCommitSha("abc123");

        GitHubRepository repository = new GitHubRepository();
        repository.setName("my-repo");
        repository.setOwnerLogin(username);
        repository.setBranches(List.of(branch));

        List<GitHubRepository> mockRepositories = List.of(repository);

        given(gitHubService.getNonForkRepositoriesWithBranches(username)).willReturn(mockRepositories);

        mockMvc.perform(get("/api/v1/github/{username}/repositories", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ownerLogin").value(username))
                .andExpect(jsonPath("$[0].branches[0].name").value("main"))
                .andExpect(jsonPath("$[0].branches[0].lastCommitSha").value("abc123"));
    }
}

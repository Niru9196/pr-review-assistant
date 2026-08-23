package com.nirupama.prreview.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GitHubClient {

    private final RestClient gitHubRestClient;

    public GitHubClient(RestClient gitHubRestClient) {
        this.gitHubRestClient = gitHubRestClient;
    }

    public List<Map<String, Object>> getPullRequestFiles(String owner, String repo, int prNumber) {
        return gitHubRestClient.get()
                .uri("/repos/{owner}/{repo}/pulls/{prNumber}/files", owner, repo, prNumber)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<Map<String, Object>>>() {});
    }
}
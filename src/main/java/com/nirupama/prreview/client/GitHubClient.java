package com.nirupama.prreview.client;

import com.nirupama.prreview.dto.PullRequestFileDto;
import com.nirupama.prreview.exception.GitHubApiException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class GitHubClient {

    private final RestClient gitHubRestClient;

    public GitHubClient(RestClient gitHubRestClient) {
        this.gitHubRestClient = gitHubRestClient;
    }

    public List<PullRequestFileDto> getPullRequestFiles(String owner, String repo, int prNumber) {
        try {
            return gitHubRestClient.get()
                    .uri("/repos/{owner}/{repo}/pulls/{prNumber}/files", owner, repo, prNumber)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<PullRequestFileDto>>() {});
        } catch (HttpClientErrorException.NotFound ex) {
            throw ex; // let the specific NotFound handler catch this
        } catch (Exception ex) {
            throw new GitHubApiException("Unable to fetch PR files for " + owner + "/" + repo + "#" + prNumber, ex);
        }
    }
}
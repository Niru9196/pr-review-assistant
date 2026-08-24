package com.nirupama.prreview.controller;

import com.nirupama.prreview.client.GitHubClient;
import com.nirupama.prreview.dto.PullRequestFileDto;
import com.nirupama.prreview.dto.PullRequestRequest;
import com.nirupama.prreview.service.ReviewService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class PullRequestController {

    private final GitHubClient gitHubClient;
    private final ReviewService reviewService;

    public PullRequestController(GitHubClient gitHubClient, ReviewService reviewService) {
        this.gitHubClient = gitHubClient;
        this.reviewService = reviewService;
    }

    @PostMapping
    public List<String> reviewPullRequest(@RequestBody PullRequestRequest request) {
        List<PullRequestFileDto> files = gitHubClient.getPullRequestFiles(
                request.owner(), request.repo(), request.prNumber()
        );
        return reviewService.reviewFiles(files);
    }
}
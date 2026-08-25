package com.nirupama.prreview.controller;

import com.nirupama.prreview.client.GitHubClient;
import com.nirupama.prreview.dto.PullRequestFileDto;
import com.nirupama.prreview.dto.PullRequestRequest;
import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.repository.ReviewRepository;
import com.nirupama.prreview.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class PullRequestController {

    private final GitHubClient gitHubClient;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    public PullRequestController(GitHubClient gitHubClient, ReviewService reviewService, ReviewRepository reviewRepository) {
        this.gitHubClient = gitHubClient;
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping
    public List<Review> reviewPullRequest(@RequestBody PullRequestRequest request) {
        List<PullRequestFileDto> files = gitHubClient.getPullRequestFiles(
                request.owner(), request.repo(), request.prNumber()
        );

        return files.stream()
                .map(file -> {
                    String reviewText = reviewService.reviewFile(file);
                    Review review = new Review(
                            request.owner(), request.repo(), request.prNumber(),
                            file.filename(), reviewText
                    );
                    return reviewRepository.save(review);
                })
                .toList();
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @GetMapping("/{owner}/{repo}/{prNumber}")
    public List<Review> getReviewsForPr(
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable int prNumber) {
        return reviewRepository.findByOwnerAndRepoAndPrNumber(owner, repo, prNumber);
    }
}
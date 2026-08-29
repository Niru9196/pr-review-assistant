package com.nirupama.prreview.controller;

import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.dto.PullRequestRequest;
import com.nirupama.prreview.repository.ReviewRepository;
import com.nirupama.prreview.review.dto.PullRequestReviewResponse;
import com.nirupama.prreview.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class PullRequestController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    public PullRequestController(
            ReviewService reviewService,
            ReviewRepository reviewRepository
    ) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping
    public PullRequestReviewResponse reviewPullRequest(
            @RequestBody PullRequestRequest request
    ) {
        return reviewService.reviewPullRequest(request);
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @GetMapping("/{owner}/{repo}/{prNumber}")
    public List<Review> getReviewsForPr(
            @PathVariable String owner,
            @PathVariable String repo,
            @PathVariable int prNumber
    ) {
        return reviewRepository.findByOwnerAndRepoAndPrNumber(
                owner,
                repo,
                prNumber
        );
    }
}
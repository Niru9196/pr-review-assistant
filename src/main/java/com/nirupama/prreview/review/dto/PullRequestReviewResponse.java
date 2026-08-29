package com.nirupama.prreview.review.dto;

import com.nirupama.prreview.entity.Review;

import java.util.List;

public record PullRequestReviewResponse(
        List<Review> reviews,
        List<String> skippedFiles
) {
}
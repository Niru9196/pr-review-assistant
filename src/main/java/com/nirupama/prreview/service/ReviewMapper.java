package com.nirupama.prreview.service;

import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.entity.ReviewFinding;
import com.nirupama.prreview.review.dto.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(
            String owner,
            String repo,
            int prNumber,
            String filename,
            ReviewResponse response
    ) {

        Review review = new Review(
                owner,
                repo,
                prNumber,
                filename,
                response.summary(),
                response.riskLevel()
        );

        response.findings()
                .forEach(finding -> {

                    ReviewFinding entity = new ReviewFinding(
                            finding.severity(),
                            finding.category(),
                            finding.file(),
                            finding.line(),
                            finding.title(),
                            finding.description(),
                            finding.suggestion(),
                            finding.confidence()
                    );

                    review.addFinding(entity);
                });

        return review;
    }
}
package com.nirupama.prreview.repository;

import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.review.dto.RiskLevel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import com.nirupama.prreview.entity.ReviewFinding;
import com.nirupama.prreview.review.dto.FindingCategory;
import com.nirupama.prreview.review.dto.Severity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void findByOwnerAndRepoAndPrNumber_returnsMatchingReviews() {

        Review review = new Review(
                "spring-projects",
                "spring-boot",
                1,
                "AstUtils.java",
                "Looks good",
                RiskLevel.LOW
        );

        reviewRepository.save(review);

        List<Review> results =
                reviewRepository.findByOwnerAndRepoAndPrNumber(
                        "spring-projects",
                        "spring-boot",
                        1
                );

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFilename())
                .isEqualTo("AstUtils.java");
        assertThat(results.get(0).getSummary())
                .isEqualTo("Looks good");
        assertThat(results.get(0).getRiskLevel())
                .isEqualTo(RiskLevel.LOW);
    }

    @Test
    void findByOwnerAndRepoAndPrNumber_returnsEmptyList_whenNoMatch() {

        List<Review> results =
                reviewRepository.findByOwnerAndRepoAndPrNumber(
                        "nonexistent",
                        "repo",
                        999
                );

        assertThat(results).isEmpty();
    }

    @Test
    void saveReview_persistsFindings() {

        Review review = new Review(
                "spring-projects",
                "spring-boot",
                1,
                "AstUtils.java",
                "Potential issue found",
                RiskLevel.HIGH
        );

        ReviewFinding finding = new ReviewFinding(
                Severity.HIGH,
                FindingCategory.BUG,
                "AstUtils.java",
                42,
                "Possible null value",
                "The code may throw an exception when the value is null.",
                "Add an appropriate null check before using the value.",
                0.95
        );

        review.addFinding(finding);

        reviewRepository.save(review);

        List<Review> results =
                reviewRepository.findByOwnerAndRepoAndPrNumber(
                        "spring-projects",
                        "spring-boot",
                        1
                );

        assertThat(results).hasSize(1);

        Review savedReview = results.get(0);

        assertThat(savedReview.getFindings())
                .hasSize(1);

        ReviewFinding savedFinding =
                savedReview.getFindings().get(0);

        assertThat(savedFinding.getSeverity())
                .isEqualTo(Severity.HIGH);

        assertThat(savedFinding.getCategory())
                .isEqualTo(FindingCategory.BUG);

        assertThat(savedFinding.getLine())
                .isEqualTo(42);

        assertThat(savedFinding.getTitle())
                .isEqualTo("Possible null value");
    }
}
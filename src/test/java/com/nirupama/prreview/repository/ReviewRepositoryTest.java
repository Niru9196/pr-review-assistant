package com.nirupama.prreview.repository;

import com.nirupama.prreview.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void findByOwnerAndRepoAndPrNumber_returnsMatchingReviews() {
        Review review = new Review("spring-projects", "spring-boot", 1, "AstUtils.java", "Looks good");
        reviewRepository.save(review);

        List<Review> results = reviewRepository.findByOwnerAndRepoAndPrNumber("spring-projects", "spring-boot", 1);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFilename()).isEqualTo("AstUtils.java");
    }

    @Test
    void findByOwnerAndRepoAndPrNumber_returnsEmptyList_whenNoMatch() {
        List<Review> results = reviewRepository.findByOwnerAndRepoAndPrNumber("nonexistent", "repo", 999);

        assertThat(results).isEmpty();
    }
}
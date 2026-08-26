package com.nirupama.prreview.controller;

import com.nirupama.prreview.client.GitHubClient;
import com.nirupama.prreview.dto.PullRequestFileDto;
import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.repository.ReviewRepository;
import com.nirupama.prreview.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PullRequestController.class)
class PullRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GitHubClient gitHubClient;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private ReviewRepository reviewRepository;

    @Test
    void getAllReviews_returnsListOfReviews() throws Exception {
        Review review = new Review("spring-projects", "spring-boot", 1, "AstUtils.java", "Looks good");
        given(reviewRepository.findAll()).willReturn(List.of(review));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename").value("AstUtils.java"));
    }

    @Test
    void reviewPullRequest_fetchesFilesAndSavesReviews() throws Exception {
        PullRequestFileDto file = new PullRequestFileDto("AstUtils.java", "modified", 1, 1, 2, "diff content");
        given(gitHubClient.getPullRequestFiles("spring-projects", "spring-boot", 1))
                .willReturn(List.of(file));
        given(reviewService.reviewFile(file)).willReturn("Looks good");
        given(reviewRepository.save(org.mockito.ArgumentMatchers.any(Review.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/api/reviews")
                        .contentType("application/json")
                        .content("""
                                {"owner": "spring-projects", "repo": "spring-boot", "prNumber": 1}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename").value("AstUtils.java"));
    }
}
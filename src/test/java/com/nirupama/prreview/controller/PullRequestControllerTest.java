package com.nirupama.prreview.controller;

import com.nirupama.prreview.dto.PullRequestRequest;
import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.repository.ReviewRepository;
import com.nirupama.prreview.review.dto.RiskLevel;
import com.nirupama.prreview.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(PullRequestController.class)
class PullRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private ReviewRepository reviewRepository;

    @Test
    void getAllReviews_returnsListOfReviews() throws Exception {

        Review review = new Review(
                "spring-projects",
                "spring-boot",
                1,
                "AstUtils.java",
                "Looks good",
                RiskLevel.LOW
        );

        given(reviewRepository.findAll())
                .willReturn(List.of(review));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename")
                        .value("AstUtils.java"))
                .andExpect(jsonPath("$[0].summary")
                        .value("Looks good"))
                .andExpect(jsonPath("$[0].riskLevel")
                        .value("LOW"));
    }

    @Test
    void reviewPullRequest_returnsSavedReviews() throws Exception {

        Review review = new Review(
                "spring-projects",
                "spring-boot",
                1,
                "AstUtils.java",
                "Looks good",
                RiskLevel.LOW
        );

        given(reviewService.reviewPullRequest(any(PullRequestRequest.class)))
                .willReturn(List.of(review));

        mockMvc.perform(
                        post("/api/reviews")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                    {
                                        "owner": "spring-projects",
                                        "repo": "spring-boot",
                                        "prNumber": 1
                                    }
                                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].filename")
                        .value("AstUtils.java"))
                .andExpect(jsonPath("$[0].summary")
                        .value("Looks good"))
                .andExpect(jsonPath("$[0].riskLevel")
                        .value("LOW"));
    }
}
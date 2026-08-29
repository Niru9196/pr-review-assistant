package com.nirupama.prreview.service;

import com.nirupama.prreview.client.GitHubClient;
import com.nirupama.prreview.dto.PullRequestFileDto;
import com.nirupama.prreview.dto.PullRequestRequest;
import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.repository.ReviewRepository;
import com.nirupama.prreview.review.dto.ReviewResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ChatClient chatClient;
    private final ReviewPromptBuilder promptBuilder;
    private final GitHubClient gitHubClient;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    public ReviewService(
            ChatClient chatClient,
            ReviewPromptBuilder promptBuilder,
            GitHubClient gitHubClient,
            ReviewMapper reviewMapper,
            ReviewRepository reviewRepository
    ) {
        this.chatClient = chatClient;
        this.promptBuilder = promptBuilder;
        this.gitHubClient = gitHubClient;
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
    }

    public ReviewResponse reviewFile(PullRequestFileDto file) {

        String prompt = promptBuilder.build(
                file.filename(),
                file.patch()
        );

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .entity(ReviewResponse.class);
    }

    public List<Review> reviewPullRequest(
            PullRequestRequest request
    ) {

        List<PullRequestFileDto> files =
                gitHubClient.getPullRequestFiles(
                        request.owner(),
                        request.repo(),
                        request.prNumber()
                );

        return files.stream()
                .map(file -> {

                    ReviewResponse response =
                            reviewFile(file);

                    Review review =
                            reviewMapper.toEntity(
                                    request.owner(),
                                    request.repo(),
                                    request.prNumber(),
                                    file.filename(),
                                    response
                            );

                    return reviewRepository.save(review);
                })
                .toList();
    }
}
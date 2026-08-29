package com.nirupama.prreview.service;

import com.nirupama.prreview.client.GitHubClient;
import com.nirupama.prreview.dto.PullRequestFileDto;
import com.nirupama.prreview.dto.PullRequestRequest;
import com.nirupama.prreview.entity.Review;
import com.nirupama.prreview.repository.ReviewRepository;
import com.nirupama.prreview.review.diff.ChangedLine;
import com.nirupama.prreview.review.diff.DiffParser;
import com.nirupama.prreview.review.dto.PullRequestReviewResponse;
import com.nirupama.prreview.review.dto.ReviewResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewPromptBuilder promptBuilder;
    private final ReviewGenerator reviewGenerator;
    private final GitHubClient gitHubClient;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;
    private final FileReviewPolicy fileReviewPolicy;
    private final DiffParser diffParser;

    public ReviewService(
            ReviewPromptBuilder promptBuilder,
            ReviewGenerator reviewGenerator,
            GitHubClient gitHubClient,
            ReviewMapper reviewMapper,
            ReviewRepository reviewRepository,
            FileReviewPolicy fileReviewPolicy,
            DiffParser diffParser
    ) {
        this.promptBuilder = promptBuilder;
        this.reviewGenerator = reviewGenerator;
        this.gitHubClient = gitHubClient;
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.fileReviewPolicy = fileReviewPolicy;
        this.diffParser = diffParser;
    }

    public ReviewResponse reviewFile(
            PullRequestFileDto file,
            List<ChangedLine> relevantLines
    ) {

        String prompt = promptBuilder.build(
                file.filename(),
                relevantLines
        );

        return reviewGenerator.generate(prompt);
    }

    public PullRequestReviewResponse reviewPullRequest(
            PullRequestRequest request
    ) {

        List<PullRequestFileDto> files =
                gitHubClient.getPullRequestFiles(
                        request.owner(),
                        request.repo(),
                        request.prNumber()
                );

        List<Review> reviews = files.stream()
                .filter(fileReviewPolicy::shouldReview)
                .map(file -> {

                    List<ChangedLine> relevantLines =
                            diffParser.parse(file.patch());

                    ReviewResponse response =
                            reviewFile(file, relevantLines);

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

        List<String> skippedFiles = files.stream()
                .filter(file -> !fileReviewPolicy.shouldReview(file))
                .map(PullRequestFileDto::filename)
                .toList();

        return new PullRequestReviewResponse(
                reviews,
                skippedFiles
        );
    }
}
package com.nirupama.prreview.service;

import com.nirupama.prreview.dto.PullRequestFileDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ChatClient chatClient;

    public ReviewService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String reviewFile(PullRequestFileDto file) {
        String prompt = buildPrompt(file);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    public List<String> reviewFiles(List<PullRequestFileDto> files) {
        return files.stream()
                .map(this::reviewFile)
                .collect(Collectors.toList());
    }

    private String buildPrompt(PullRequestFileDto file) {
        return """
                You are a senior software engineer reviewing a pull request.
                Review the following code diff and provide concise, actionable feedback.
                Focus on bugs, code quality, and potential issues. Keep it brief.

                File: %s
                Diff:
                %s
                """.formatted(file.filename(), file.patch());
    }
}
package com.nirupama.prreview.service;

import com.nirupama.prreview.exception.ReviewGenerationException;
import com.nirupama.prreview.review.dto.ReviewResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ReviewGenerator {

    private final ChatClient chatClient;

    public ReviewGenerator(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public ReviewResponse generate(String prompt) {
        try {
            return chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .entity(ReviewResponse.class);
        } catch (Exception ex) {
            throw new ReviewGenerationException(
                    "LLM failed to produce a valid review: " + ex.getMessage(), ex);
        }
    }
}
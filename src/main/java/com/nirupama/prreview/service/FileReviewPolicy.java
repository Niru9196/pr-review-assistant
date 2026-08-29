package com.nirupama.prreview.service;

import com.nirupama.prreview.dto.PullRequestFileDto;
import org.springframework.stereotype.Component;

@Component
public class FileReviewPolicy {

    public boolean shouldReview(PullRequestFileDto file) {

        if (file.patch() == null || file.patch().isBlank()) {
            return false;
        }

        String filename = file.filename().toLowerCase();

        return filename.endsWith(".java")
                || filename.endsWith(".js")
                || filename.endsWith(".ts")
                || filename.endsWith(".py")
                || filename.endsWith(".go")
                || filename.endsWith(".tsx")
                || filename.endsWith(".jsx");
    }
}
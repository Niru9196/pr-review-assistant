package com.nirupama.prreview.dto;

public record PullRequestRequest(
        String owner,
        String repo,
        int prNumber
) {}
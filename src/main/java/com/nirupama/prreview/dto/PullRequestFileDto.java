package com.nirupama.prreview.dto;

public record PullRequestFileDto(
        String filename,
        String status,
        int additions,
        int deletions,
        int changes,
        String patch
) {}
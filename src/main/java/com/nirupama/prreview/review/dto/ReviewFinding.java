package com.nirupama.prreview.review.dto;

public record ReviewFinding(
        Severity severity,
        FindingCategory category,
        String file,
        Integer line,
        String title,
        String description,
        String suggestion,
        Double confidence
) {
}

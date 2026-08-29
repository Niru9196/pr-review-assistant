package com.nirupama.prreview.review.diff;

public record ChangedLine(
        int lineNumber,
        String content,
        LineType type
) {
}
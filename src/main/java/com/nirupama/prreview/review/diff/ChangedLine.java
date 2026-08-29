package com.nirupama.prreview.review.diff;

public record ChangedLine(
        Integer oldLineNumber,
        Integer newLineNumber,
        String content,
        LineType type
) {
}
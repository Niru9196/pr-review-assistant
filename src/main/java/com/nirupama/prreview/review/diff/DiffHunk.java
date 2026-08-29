package com.nirupama.prreview.review.diff;

import java.util.List;

public record DiffHunk(
        int startLine,
        List<ChangedLine> changedLines
) {
}
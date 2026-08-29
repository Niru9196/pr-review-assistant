package com.nirupama.prreview.review.dto;

import java.util.List;

public record ReviewResponse(
        String summary,
        RiskLevel riskLevel,
        List<ReviewFinding> findings
) {
}

package com.nirupama.prreview.service;

import org.springframework.stereotype.Component;

@Component
public class ReviewPromptBuilder {

    public String build(String filename, String patch) {
        return """
            You are an expert software engineer performing a code review.

            Review the following changed file from a GitHub Pull Request.

            File:
            %s

            Changed code:
            %s

            Analyze the changes and identify only actionable issues.

            For each issue provide:
            - severity
            - category
            - file
            - line
            - title
            - description
            - suggestion
            - confidence

            Also provide:
            - summary
            - overall risk level

            Rules:
            - Only report real, actionable issues.
            - Do not report purely stylistic preferences.
            - Do not invent files.
            - Do not invent line numbers.
            - Confidence must be between 0 and 1.
            - If there are no issues, return an empty findings list.
            
            Return ONLY structured JSON in this format:
            
            {
              "summary": "Short overall review summary",
              "riskLevel": "LOW",
              "findings": [
                {
                  "severity": "MEDIUM",
                  "category": "BUG",
                  "file": "example.java",
                  "line": 42,
                  "title": "Short issue title",
                  "description": "Explain the issue",
                  "suggestion": "Explain how to fix it",
                  "confidence": 0.90
                }
              ]
            }
            
            Allowed risk levels:
            LOW, MEDIUM, HIGH, CRITICAL
            
            Allowed severities:
            LOW, MEDIUM, HIGH, CRITICAL
            
            Allowed categories:
            BUG, SECURITY, PERFORMANCE, MAINTAINABILITY, TESTING
            
            If there are no issues:
            
            {
              "summary": "No significant issues found.",
              "riskLevel": "LOW",
              "findings": []
            }
            """.formatted(filename, patch);
    }
}
package com.nirupama.prreview.service;

import com.nirupama.prreview.review.diff.ChangedLine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewPromptBuilder {

    public String build(
            String filename,
            List<ChangedLine> lines
    ) {

        String code = lines.stream()
                .map(line -> String.format(
                        "old=%s | new=%s | %-8s | %s",
                        line.oldLineNumber() == null
                                ? "-"
                                : line.oldLineNumber(),
                        line.newLineNumber() == null
                                ? "-"
                                : line.newLineNumber(),
                        line.type(),
                        line.content()
                ))
                .collect(Collectors.joining("\n"));

        return """
                You are a senior software engineer performing a code review.

                Review the following changed section of:

                File: %s

                Code:

                %s

                Line number semantics:
                - old = line number in the previous version of the file
                - new = line number in the current version of the file
                - ADDED lines have an old line number of "-"
                - REMOVED lines have a new line number of "-"
                - CONTEXT lines have both old and new line numbers

                The lines marked ADDED or REMOVED are changes.
                Lines marked CONTEXT are surrounding code provided to help
                understand the change.

                Focus primarily on ADDED code.

                For findings on ADDED or CONTEXT code, use the NEW line number
                when reporting the line.

                For findings specifically about REMOVED code, use the OLD
                line number.

                Prioritize:
                - correctness
                - bugs
                - security
                - performance
                - reliability
                - maintainability
                - testing

                Do not report:
                - formatting-only issues
                - whitespace issues
                - missing trailing newlines
                - subjective style preferences
                - unchanged code unless it directly causes a problem
                - issues unrelated to the current change

                Only report concrete and actionable problems.

                For every finding provide:
                - severity
                - category
                - file
                - line
                - title
                - description
                - suggestion
                - confidence between 0 and 1

                If there are no meaningful issues, return an empty findings list.
                """.formatted(
                filename,
                code
        );
    }
}
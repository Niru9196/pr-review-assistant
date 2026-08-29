package com.nirupama.prreview.review.diff;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DiffParser {

    private static final Pattern HUNK_PATTERN =
            Pattern.compile(
                    "^@@ -(\\d+)(?:,\\d+)? \\+(\\d+)(?:,\\d+)? @@.*$"
            );

    private static final int CONTEXT_LINES = 3;

    public List<ChangedLine> parse(String patch) {

        if (patch == null || patch.isBlank()) {
            return List.of();
        }

        List<ChangedLine> lines = new ArrayList<>();

        int currentLine = 0;

        for (String line : patch.split("\n")) {

            Matcher matcher = HUNK_PATTERN.matcher(line);

            if (matcher.matches()) {
                currentLine = Integer.parseInt(matcher.group(2));
                continue;
            }

            if (line.startsWith("\\ No newline")) {
                continue;
            }

            if (line.startsWith("+++")
                    || line.startsWith("---")) {
                continue;
            }

            if (line.startsWith("+")) {

                lines.add(
                        new ChangedLine(
                                currentLine,
                                line.substring(1),
                                LineType.ADDED
                        )
                );

                currentLine++;
                continue;
            }

            if (line.startsWith("-")) {

                lines.add(
                        new ChangedLine(
                                currentLine,
                                line.substring(1),
                                LineType.REMOVED
                        )
                );

                continue;
            }

            if (line.startsWith(" ")) {

                lines.add(
                        new ChangedLine(
                                currentLine,
                                line.substring(1),
                                LineType.CONTEXT
                        )
                );

                currentLine++;
            }
        }

        return selectRelevantLines(lines);
    }

    private List<ChangedLine> selectRelevantLines(
            List<ChangedLine> lines
    ) {

        List<Integer> changedIndexes = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            LineType type = lines.get(i).type();

            if (type == LineType.ADDED
                    || type == LineType.REMOVED) {

                changedIndexes.add(i);
            }
        }

        if (changedIndexes.isEmpty()) {
            return List.of();
        }

        List<ChangedLine> result = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            boolean relevant = false;

            for (Integer changedIndex : changedIndexes) {

                if (Math.abs(i - changedIndex) <= CONTEXT_LINES) {
                    relevant = true;
                    break;
                }
            }

            if (relevant) {
                result.add(lines.get(i));
            }
        }

        return result;
    }
}
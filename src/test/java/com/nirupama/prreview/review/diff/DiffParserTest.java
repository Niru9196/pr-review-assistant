package com.nirupama.prreview.review.diff;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiffParserTest {

    private final DiffParser diffParser = new DiffParser();

    @Test
    void parsesRemovedLine() {

        String patch = """
                @@ -10,4 +10,4 @@
                 line 10
                -old line
                +new line
                 line 12
                """;

        List<ChangedLine> result = diffParser.parse(patch);

        assertThat(result)
                .filteredOn(line -> line.type() == LineType.REMOVED)
                .hasSize(1);

        assertThat(result)
                .filteredOn(line -> line.type() == LineType.REMOVED)
                .extracting(ChangedLine::content)
                .containsExactly("old line");
    }

    @Test
    void parsesMultipleHunks() {

        String patch = """
                @@ -10,3 +10,3 @@
                 line 10
                -old line
                +new line
                 line 12
                @@ -30,3 +30,3 @@
                 line 30
                -old another line
                +new another line
                 line 32
                """;

        List<ChangedLine> result = diffParser.parse(patch);

        assertThat(result)
                .filteredOn(line -> line.type() == LineType.ADDED)
                .extracting(ChangedLine::content)
                .containsExactly(
                        "new line",
                        "new another line"
                );

        assertThat(result)
                .filteredOn(line -> line.type() == LineType.REMOVED)
                .extracting(ChangedLine::content)
                .containsExactly(
                        "old line",
                        "old another line"
                );
    }

    @Test
    void parsesChangedLinesWithOldAndNewLineNumbers() {

        String patch = """
                @@ -10,5 +10,5 @@
                 line 10
                -old line 11
                 line 12
                +new line
                 line 14
                """;

        List<ChangedLine> result = diffParser.parse(patch);

        assertThat(result).hasSize(5);

        // context: line 10
        assertThat(result.get(0).oldLineNumber())
                .isEqualTo(10);
        assertThat(result.get(0).newLineNumber())
                .isEqualTo(10);
        assertThat(result.get(0).content())
                .isEqualTo("line 10");
        assertThat(result.get(0).type())
                .isEqualTo(LineType.CONTEXT);

        // removed: old line 11
        assertThat(result.get(1).oldLineNumber())
                .isEqualTo(11);
        assertThat(result.get(1).newLineNumber())
                .isNull();
        assertThat(result.get(1).content())
                .isEqualTo("old line 11");
        assertThat(result.get(1).type())
                .isEqualTo(LineType.REMOVED);

        // context: old line 12 -> new line 11
        assertThat(result.get(2).oldLineNumber())
                .isEqualTo(12);
        assertThat(result.get(2).newLineNumber())
                .isEqualTo(11);
        assertThat(result.get(2).content())
                .isEqualTo("line 12");
        assertThat(result.get(2).type())
                .isEqualTo(LineType.CONTEXT);

        // added: new line 12
        assertThat(result.get(3).oldLineNumber())
                .isNull();
        assertThat(result.get(3).newLineNumber())
                .isEqualTo(12);
        assertThat(result.get(3).content())
                .isEqualTo("new line");
        assertThat(result.get(3).type())
                .isEqualTo(LineType.ADDED);

        // context: old line 13 -> new line 13
        assertThat(result.get(4).oldLineNumber())
                .isEqualTo(13);
        assertThat(result.get(4).newLineNumber())
                .isEqualTo(13);
        assertThat(result.get(4).content())
                .isEqualTo("line 14");
        assertThat(result.get(4).type())
                .isEqualTo(LineType.CONTEXT);
    }
}
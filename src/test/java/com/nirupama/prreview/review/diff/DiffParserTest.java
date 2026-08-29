package com.nirupama.prreview.review.diff;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DiffParserTest {

    private final DiffParser diffParser = new DiffParser();

    @Test
    void parsesChangedLinesAndContext() {

        String patch = """
                @@ -10,7 +10,7 @@
                 line 10
                 line 11
                 line 12
                +new line
                 line 14
                 line 15
                 line 16
                """;

        List<ChangedLine> result = diffParser.parse(patch);

        assertThat(result).hasSize(7);

        assertThat(result.get(0).lineNumber()).isEqualTo(10);
        assertThat(result.get(0).type()).isEqualTo(LineType.CONTEXT);

        assertThat(result.get(1).lineNumber()).isEqualTo(11);
        assertThat(result.get(1).type()).isEqualTo(LineType.CONTEXT);

        assertThat(result.get(2).lineNumber()).isEqualTo(12);
        assertThat(result.get(2).type()).isEqualTo(LineType.CONTEXT);

        assertThat(result.get(3).lineNumber()).isEqualTo(13);
        assertThat(result.get(3).type()).isEqualTo(LineType.ADDED);
        assertThat(result.get(3).content()).isEqualTo("new line");

        assertThat(result.get(4).lineNumber()).isEqualTo(14);
        assertThat(result.get(5).lineNumber()).isEqualTo(15);
        assertThat(result.get(6).lineNumber()).isEqualTo(16);
    }

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
}
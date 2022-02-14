package net.spandigital.presidium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.spandigital.presidium.IO.readText;
import static net.spandigital.presidium.Markdown.editFrontMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MarkdownTest {

    @TempDir
    private File workDir;
    private File markdownFile;


    @AfterEach
    void cleanup() {
        markdownFile.delete();
    }


    @Test
    void editNonExistingFile() throws IOException {
        markdownFile = new File(workDir, "non-existing.md");
        editFrontMapper(markdownFile.toPath(), fm -> {
            fm.put("counter", 1);
            fm.put("name", "name");
            List<String> roles = new ArrayList<>();
            roles.add("admin");
            roles.add("reader");
            fm.put("roles", roles);
        });
        assertTrue(markdownFile.exists());
        assertTrue(markdownFile.length() > 0, "expected non empty file: " + markdownFile);
        String actual = readText(markdownFile);
        String expected = "---\n" +
                "counter: 1\n" +
                "name: name\n" +
                "roles:\n" +
                "- admin\n" +
                "- reader\n" +
                "---\n";
        assertEquals(expected, actual);
    }

    @Test
    void removingOfFrontMatterShouldHaveNoMarkup() throws IOException {
        markdownFile = Paths.requiredFile(new File(workDir, "markdown-with-content.md").toPath());
        try (PrintWriter p = new PrintWriter(new FileWriter(markdownFile))) {
            printTestFrontMatterRaw(p);
            printTestContent(p);
        }

        editFrontMapper(markdownFile.toPath(), Map::clear);
        String actual = readText(markdownFile);
        assertEquals(
                "## Hello\n" +
                        "\n" +
                        "World\n", actual);
    }

    @Test
    void addingMoreFrontMatterShouldNotOverwriteContent() throws IOException {
        markdownFile = new File(workDir, "some.md");
        try (PrintWriter p = new PrintWriter(new FileWriter(markdownFile))) {
            printTestContent(p);
        }

        editFrontMapper(markdownFile.toPath(), fm -> {
            fm.put("slug", "slug");
        });

        String actual = readText(markdownFile);
        String expected =
                "---\n"
                        + "slug: slug\n"
                        + "---\n"
                        + "## Hello\n"
                        + "\n"
                        + "World\n";

        assertEquals(expected, actual);
    }

    private static void printTestContent(PrintWriter p) {
        p.println("## Hello");
        p.println("");
        p.println("World");
    }

    private static void printTestFrontMatterRaw(PrintWriter p) {
        p.println("---");
        p.println("title: this is a title");
        p.println("slug: this-is-a-title");
        p.println("---");
    }


}
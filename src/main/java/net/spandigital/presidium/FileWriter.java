package net.spandigital.presidium;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by paco on 2017/05/26.
 */
public class FileWriter {

    public static String fileName(int order, String name) {
        return String.format("%03d-%s.md", order, name);
    }

    public static void writeIndex(Path path, String title) {
        write(path.resolve("index.md"), Markdown.frontMatter(title));
    }

    public static void write(Path file, StringBuffer content) {
        write(file, content.toString());
    }

    public static void write(Path file, String content) {
        System.out.printf("Writing: %s\n", file);
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.print(e);
        }
    }
}

package net.spandigital.presidium;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import static net.spandigital.presidium.Markdown.slugify;

/**
 * Created by paco on 2017/05/26.
 */
public class FileWriter {

    public static String fileName(int order, String name) {
        return String.format("%s.md", name);
    }

    public static void writeIndex(Path path, String title) {
        Markdown.editFrontMapper(path.resolve("index.md"), (a) -> {
            a.put("title", title);
            a.put("slug", slugify(title));
        });
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

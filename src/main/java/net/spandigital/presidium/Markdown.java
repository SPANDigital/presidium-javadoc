package net.spandigital.presidium;

import com.sun.javadoc.Doc;
import com.sun.javadoc.ProgramElementDoc;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static net.spandigital.presidium.IO.copy;
import static net.spandigital.presidium.IO.skipLines;
import static net.spandigital.presidium.Paths.requiredFile;

/**
 * Markdown generation methods.
 *
 * @author Paco Mendes
 */
public class Markdown {

    public static final String FRONT_MATTER_DELIMITER = "---";

    static Yaml yaml() {
        DumperOptions d = new DumperOptions();
        d.setPrettyFlow(true);
        d.setExplicitStart(true);
        d.setExplicitEnd(false);
        d.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return new Yaml(d);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void editFrontMapper(Path path, Consumer<Map<Object, Object>> editor) {

        File file = requiredFile(path);
        Yaml yaml = yaml();
        Map<Object, Object> frontMatter = null;
        String frontMatterContent = null;
        int frontMatterRows = 0;
        String updatedFrontMatter = null;

        try (RandomAccessFile source = new RandomAccessFile(file, "rw")) {

            StringBuilder sb = new StringBuilder();
            String line = source.readLine();
            boolean readingFrontMatter = line != null && (line.trim()).equals(FRONT_MATTER_DELIMITER);
            boolean frontMatterFound = false;

            if (readingFrontMatter) {
                while (true) {
                    line = source.readLine();
                    if (line == null) break;
                    line = line.trim();
                    if (line.equals(FRONT_MATTER_DELIMITER)) {
                        frontMatterFound = true;
                        break;
                    }
                    sb.append(line).append(newLine());
                    ++frontMatterRows;
                }
            }

            String existingFrontMatter;
            if (frontMatterFound) {
                existingFrontMatter = sb.toString();
                frontMatter = yaml.load(existingFrontMatter);
                frontMatterContent = yaml.dump(frontMatter);
                frontMatterRows += 2;
            } else {
                frontMatterRows = 0;
                frontMatter = new LinkedHashMap<>();
            }

            editor.accept(frontMatter);
            updatedFrontMatter = yaml.dump(frontMatter);

            boolean updated = !Objects.equals(frontMatterContent, updatedFrontMatter);

            if (updated) {
                File temp = Files.createTempFile(null, null).toFile();
                try {
                    try (PrintWriter edit = new PrintWriter(new BufferedWriter(new FileWriter(temp)))) {
                        if (frontMatter.size() > 0) {
                            edit.print(updatedFrontMatter);
                            edit.println(FRONT_MATTER_DELIMITER);
                        }
                        source.seek(0);
                        if (frontMatterRows > 0) {
                            skipLines(source, frontMatterRows);
                        }
                        while (true) {
                            line = source.readLine();
                            if (line == null) {
                                break;
                            }
                            edit.println(line);
                        }
                    }
                    source.seek(0);
                    copy(temp, source);
                    source.setLength(temp.length());
                } finally {
                    temp.delete();
                }
            }

        } catch (IOException e) {
            throw new IO.Exception("failure editing front matter", e);
        }


    }

    public static String slugify(String str) {
        return str.toLowerCase().replaceAll("\\W+", "-");
    }

    public static String frontMatter(String title, String slug) {
        return Markdown.lines(
                FRONT_MATTER_DELIMITER,
                format("%s: \"%s\"", "title", title),
                format("%s: \"%s\"", "slug", slug),
                FRONT_MATTER_DELIMITER
        );
    }

    public static String frontMatter(String title) {
        return frontMatter(title, slugify(title));
    }

    public static String lines(String... lines) {
        return Arrays.stream(lines).collect(joining(newLine()));
    }

    public static String h1(String title) {
        return h(1, title);
    }

    public static String h2(String title) {
        return h(2, title);
    }

    public static String h3(String title) {
        return h(3, title);
    }

    private static String h(int level, String title) {
        return newLine() + String.join("", Collections.nCopies(level, "#")) + " " + title + newLine(2);
    }

    public static String quote(String text) {
        return format("%s> %s%s", Markdown.newLine(), text, Markdown.newLine());
    }

    public static String hr() {
        return Markdown.newLine(2) + FRONT_MATTER_DELIMITER + Markdown.newLine();
    }

    public static String anchor(ProgramElementDoc element) {
        return format("<span class=\"anchor\" id=\"%s\"></span>", element.qualifiedName());
    }

    public static String link(String value, String target) {
        return format("[%s](%s)", value, target);
    }

    public static String siteLink(String value, String target) {
        return format("[%s]({{'%s' | relative_url }})", value, target);
    }

    public static String anchorLink(String value, String target) {
        return format("[%s](#%s)", value, target);
    }

    public static String newLine() {
        return System.lineSeparator();
    }

    public static String newLine(int count) {
        return String.join("", Collections.nCopies(count, newLine()));
    }

    public static String tableHeader(String... titles) {
        StringBuilder header = new StringBuilder();
        for (String title : titles) {
            header.append(format("| %s ", title));
        }
        header.append(Markdown.newLine());
        header.append(String.join("", Collections.nCopies(titles.length, "|:---")));
//        header.append(Markdown.newLine());
        return header.toString();
    }

    public static String tableRow(String... values) {
        return tableRow(Arrays.asList(values));
    }

    public static String tableRow(Collection<String> values) {
        return values.stream()
                .map(v -> format("|%s ", v))
                .collect(joining()) + Markdown.newLine();
    }

    public static String docSummary(Doc doc) {
        //TODO improve for other edge cases
        return new BufferedReader(new StringReader(doc.commentText()))
                .lines()
                .filter(l -> !l.equalsIgnoreCase("<p>"))
                .findFirst()
                .orElse("");
    }

    public static String docComment(Doc doc) {
        //TODO parse and edit tags
        //doc.inlineTags();
        //Simple cleanup to assist with layout
        return doc.commentText()
                .replaceAll("<p>", Markdown.newLine())
                .replaceAll("</p>", "")
                .replaceAll("<ul>", "")
                .replaceAll("</ul>", "")
                .replaceAll("</li>", "")
                .replaceAll("<li>", "- ");
    }

}

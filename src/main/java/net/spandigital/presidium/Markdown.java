package net.spandigital.presidium;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by paco on 2017/05/26.
 */
public class Markdown {

    public static String frontMatter(String title) {
        return String.format(
                "---" +
                        "\n" +
                        "title: %s" +
                        "\n" +
                        "---" +
                        "\n",

                title);
    }

    public static String join(String... elements) {
        return Arrays.stream(elements).collect(Collectors.joining(newLine()));
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

//    public static String h2(String header) {
//        return newLine()"\n\n## " + header + newLine();
//    }
//
//    public static String h3(String header) {
//        return "\n\n### " + header + "\n\n";
//    }

    public static String fileName(int order, String name) {
        return String.format("%03d-%s.md", order, name);
    }

    public static String link(String target, String value) {
        return String.format("[%s](%s)", value, target);
    }

    public static String linkAnchor(String target, String value) {
        return String.format("[%s](#%s)", value, target);
    }

    public static String newLine() {
        return System.lineSeparator();
    }

    public static String newLine(int count) {
        return String.join("", Collections.nCopies(count, newLine()));
    }

    public static String tableHeader(String... titles) {
        StringBuffer header = new StringBuffer();
        for (String title : titles) {
            header.append(String.format("| %s ", title));
        }
        header.append(Markdown.newLine());
        header.append(String.join("", Collections.nCopies(titles.length, "|:---")));
        header.append(Markdown.newLine());
        return header.toString();
    }

    public static String tableRow(String... values) {
        return tableRow(Arrays.asList(values));
    }

    public static String tableRow(Collection<String> values) {
        return values.stream()
                .map(v -> String.format("|%s ", v))
                .collect(Collectors.joining()) + Markdown.newLine();
    }

    public static String parseContent(String content) {
        return "";
    }


}

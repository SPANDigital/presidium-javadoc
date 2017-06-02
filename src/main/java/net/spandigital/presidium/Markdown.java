package net.spandigital.presidium;

import com.sun.javadoc.Doc;
import com.sun.javadoc.ProgramElementDoc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by paco on 2017/05/26.
 */
public class Markdown {

    public static String frontMatter(String title) {
        return Markdown.join(
                "---",
                "title: " + title,
                "---",
                Markdown.newLine()
        );
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


    public static String quote(String text) {
        return String.format("%s> %s%s", Markdown.newLine(), text, Markdown.newLine());
    }

    public static String hr() {
        return Markdown.newLine(2) + "---" + Markdown.newLine();
    }

    public static String fileName(int order, String name) {
        return String.format("%03d-%s.md", order, name);
    }

    public static String anchor(ProgramElementDoc element) {
        return String.format("<span class=\"anchor\" id=\"%s\"></span>", element.qualifiedName());
    }

    public static String link(String value, String target) {
        return String.format("[%s](%s)", value, target);
    }

    public static String siteLink(String value, String target) {
        return String.format("[%s]({{'%s' | relative_url }})", value, target);
    }

    public static String anchorLink(String value, String target) {
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

    public static String summary(Doc doc) {
        //TODO improve for other edge cases
        return new BufferedReader(new StringReader(doc.commentText()))
                .lines()
                .filter(l -> !l.equalsIgnoreCase("<p>"))
                .findFirst()
                .orElse("");
    }

    public static String content(Doc doc) {
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

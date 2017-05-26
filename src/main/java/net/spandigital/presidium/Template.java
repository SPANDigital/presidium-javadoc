package net.spandigital.presidium;

import com.sun.javadoc.Doc;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by paco on 2017/05/26.
 */
public class Template {

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

    public static String h1(String header) {
        return "\n\n# " + header + "\n\n";
    }

    public static String h2(String header) {
        return "\n\n## " + header + "\n\n";
    }

    public static String h3(String header) {
        return "\n\n### " + header + "\n\n";
    }

    public static String fileName(int order, String name) {
        return String.format("%03d-%s.md", order, name);
    }

    public static String firstLine(Doc doc) {
        return new BufferedReader(new StringReader(doc.commentText()))
                .lines()
                .filter(l -> !l.equalsIgnoreCase("<p>"))
                .findFirst()
                .orElse("");
    }
}

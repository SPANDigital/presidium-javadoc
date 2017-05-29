package net.spandigital.presidium;

import com.sun.javadoc.Doc;

import java.io.BufferedReader;
import java.io.StringReader;

/**
 * Created by paco on 2017/05/29.
 */
public class Comment {

    private String summary;
    private String body;

    public static Comment parse(Doc doc) {
        Comment javadoc = new Comment();
        javadoc.summary = summary(doc);
        javadoc.body = body(doc);
        return javadoc;
    }


    private static String summary(Doc doc) {
        //TODO improve for other edge cases
        return new BufferedReader(new StringReader(doc.commentText()))
                .lines()
                .filter(l -> !l.equalsIgnoreCase("<p>"))
                .findFirst()
                .orElse("");
    }

    private static String body(Doc doc) {
        //TODO parse and edit tags
        doc.inlineTags();
        return doc.commentText();
    }

    public String getSummary() {
        return summary;
    }

    public String getBody() {
        return body;
    }

}

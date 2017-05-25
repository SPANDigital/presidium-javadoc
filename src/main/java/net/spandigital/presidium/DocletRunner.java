/**
 * Created by paco on 2017/05/24.
 */
package net.spandigital.presidium;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class DocletRunner {

    public static void main(String[] args) throws IOException {
        buildMarkdown();
//        buildJavadoc();
    }

    private static void buildMarkdown() throws IOException {
        String targetDir = System.getProperty("user.dir") + "/javadoc-output";
        Files.createDirectories(FileSystems.getDefault().getPath(targetDir));

        /*
        This method of execution allows us to avoid providing classpath and also to use relative directory references
        Params:
            program name
            doclet name
            source path - our own source
            subpackages - one of our own packages
            output - a directory, relative to the project
        */
        Main.execute("Javadoc-MD", "net.spandigital.presidium.Doclet", new String[]{
//                "-sourcepath", System.getProperty("user.dir") + "/src/main/java",
//                "-sourcepath", "/Users/paco/Workspace/span/presidium/javadoc/doclet/mt-common-services/mt-common-services-client/src/main/java",
                "-sourcepath", "/Users/paco/Workspace/span/presidium/javadoc/doclet/src/main/java",

                "-subpackages", "com.sun.tools",
                "-d", System.getProperty("user.dir") + "/docs/presidium"
        });
    }

    private static void buildJavadoc() {
        Main.execute(new String[]{
                "-sourcepath", "/Users/paco/Workspace/span/presidium/javadoc/doclet/src/main/java",
                "-subpackages", "com.sun.tools",
//                "-group", "html", "com.outerthoughts.javadoc.iframed.formats.html:com.outerthoughts.javadoc.iframed.formats.html.markup",
//                "-group", "internal", "com.outerthoughts.javadoc.iframed.internal.toolkit:com.outerthoughts.javadoc.iframed.internal.toolkit.builders:com.outerthoughts.javadoc.iframed.internal.toolkit.taglets:com.outerthoughts.javadoc.iframed.internal.toolkit.util:com.outerthoughts.javadoc.iframed.internal.toolkit.util.links",
//                "-use", "-nohelp", "-splitindex",
                "-d", System.getProperty("user.dir") + "/docs/javadoc"
        });

    }
}
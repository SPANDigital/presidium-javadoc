/**
 * Created by paco on 2017/05/24.
 */
package net.spandigital.presidium;

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
        Main.execute("Markdown Generator", "net.spandigital.presidium.Doclet", new String[]{
                "-sourcepath", System.getProperty("user.dir") + "/src/test/resources",
                "-subpackages", "java.time",
//                "-d", System.getProperty("user.dir") + "/docs/test"
                "-d", "/Users/paco/Workspace/span/presidium/presidium-pm/content/_reference/Time API"
        });
    }

    private static void buildJavadoc() {
        Main.execute(new String[]{
                "-sourcepath", "/Users/paco/Workspace/span/presidium/javadoc/doclet/src/main/java",
                "-subpackages", "java.time",
                "-d", System.getProperty("user.dir") + "/docs/javadoc"
        });

    }
}
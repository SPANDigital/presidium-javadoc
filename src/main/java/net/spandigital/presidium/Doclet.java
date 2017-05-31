/**
 * Created by paco on 2017/05/24.
 */
package net.spandigital.presidium;

import com.sun.javadoc.*;
import com.sun.tools.javac.util.List;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Doclet {

    private static final List<String> opts = List.of("-d", "-t", "-u");//, "-doctitle", "-windowtitle");

    public static boolean start(RootDoc root) throws IOException {
        Path destination = Paths.get(option(root, "-d", "docs"));
        String title = option(root, "-t", "javadoc");
        String url = option(root, "-u", "reference/javadoc");

        clean(destination);

        Files.createDirectories(destination);

        PackageWriter.init(root, destination.resolve("01-Packages"), url)
                .writeAll();

        ClassWriter.init(root, destination.resolve("02-Classes"), url)
                .writeAll();

        FileWriter.writeIndex(destination, title);

        return true;
    }

    /**
     * Allow custom doclet opts
     * @param option
     * @return
     */
    public static int optionLength(String option) {
        if (opts.contains(option)) {
            return 2;
        }
        return 0;
    }

    private static String option(RootDoc root, String option, String defaultValue) {
        for (String[] opt : root.options()) {
            if (opt[0].equals(option)) {
                return opt[1];
            }
        }
        return defaultValue;
    }

    private static void clean(Path target) throws IOException {
        System.out.println(String.format("Cleaning: %s", target));
        if (!Files.exists(target)) {
            return;
        }
        Files.walk(target)
                .map(Path::toFile)
                .forEach(File::delete);
    }

}
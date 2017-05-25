/**
 * Created by paco on 2017/05/24.
 */
package net.spandigital.presidium;

import com.sun.javadoc.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Doclet {

    public static boolean start(RootDoc root) throws IOException {

        Path target = targetPath(root);
        clean(target);
        writeArticles(target, root.classes());
        return true;
    }

    public static int optionLength(String option) {
        if (option.equals("-d") || option.equals("-doctitle") || option.equals("-windowtitle")) {
            return 2;
        }
        return 0;
    }

    private static Path targetPath(RootDoc root) {
        for (String[] opt : root.options()) {
            if (opt[0].equals("-d")) {
                return Paths.get(opt[1]);
            }
        }
        return Paths.get(System.getProperty("user.dir") + "/docs/presidium");
    }

    private static void clean(Path target) throws IOException {
        Files.walk(target)
                .map(Path::toFile)
                .peek(System.out::println)
                .forEach(File::delete);
    }

    private static void writeArticles(Path target, ClassDoc[] classes) throws IOException {
        for (ClassDoc cls : classes) {
            ArticleWriter.write(target, cls);
        }
    }


}
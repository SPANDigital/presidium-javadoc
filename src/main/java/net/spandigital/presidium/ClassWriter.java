package net.spandigital.presidium;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Paco Mendes
 */
public class ClassWriter {

    public static void write(Path target, RootDoc root) throws IOException {

        List<ClassDoc> classes = Arrays.stream(root.classes())
                .sorted(Comparator.comparing(ClassDoc::name))
                .collect(Collectors.toList());

        Path dir = target.resolve("Classes");
        Files.createDirectories(dir);

        int i = 0;
        for (ClassDoc cls : classes) {
            Path file = dir.resolve(Template.fileName(i++, cls.name()));
            ClassWriter.write(file, cls);
        }
    }

    public static void write(Path file, ClassDoc cls) throws IOException {
        StringBuffer content = new StringBuffer();
        content.append(Template.frontMatter(cls.name()));
        content.append(System.lineSeparator());
        content.append(description(cls));
        FileWriter.write(file, content);
    }

    private static String title(ClassDoc cls) {
        return String.format("# %s", cls.name());
    }

    private static String description(ClassDoc cls) {
        return cls.commentText();
    }

}

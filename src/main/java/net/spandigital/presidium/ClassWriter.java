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

    public static void writeAll(Path target, RootDoc root) throws IOException {

        List<ClassDoc> classes = Arrays.stream(root.classes())
                .sorted(Comparator.comparing(ClassDoc::name))
                .collect(Collectors.toList());

        Files.createDirectories(target);
        FileWriter.writeIndex(target, "Classes");

        int i = 0;
        for (ClassDoc cls : classes) {
            Path file = target.resolve(Markdown.fileName(i++, cls.name()));
            ClassWriter.write(file, cls);
        }
    }

    public static void write(Path file, ClassDoc cls) throws IOException {
        FileWriter.write(file, Markdown.join(
                Markdown.frontMatter(cls.name()),
                header(cls),
                description(cls)
        ));
    }

    private static String header(ClassDoc cls) {
        String containingPackage = cls.containingPackage().name();
        return Markdown.join(
                Markdown.linkSite(containingPackage, "/packages/#" + containingPackage),
                Markdown.h1(title(cls))
        );
    }

    private static String title(ClassDoc cls) {
        switch(classType(cls)) {
            case CLASS:
                return cls.modifiers() + " class " + cls.name();
            case ENUM:
                return cls.modifiers() + " enum " + cls.name();
            case INTERFACE:
                return cls.modifiers() + " " + cls.name();
            default:
                return cls.name();
        }
    }

    private static String extendsClass(ClassDoc cls) {
        if (!cls.isInterface()) {
            return cls.superclass().name();
        }
        return "";
    }

    private enum ClassType {
        CLASS,
        ENUM,
        INTERFACE,
        UNKNOWN
    }

    private static ClassType classType(ClassDoc cls) {

        if (cls.superclass() != null && "java.lang.Enum".equals(cls.superclass().qualifiedName())) {
            return ClassType.ENUM;
        }

        if (cls.isOrdinaryClass()) {
            return ClassType.CLASS;
        }

        if (cls.isInterface()) {
            return ClassType.INTERFACE;
        }
        return ClassType.UNKNOWN;
    }

    private static String description(ClassDoc cls) {
        return cls.commentText();
    }

}

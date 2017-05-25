package net.spandigital.presidium;

import com.sun.javadoc.ClassDoc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by paco on 2017/05/25.
 */
public class ArticleWriter {

    public static void write(Path target, ClassDoc cls) throws IOException {

        Path dir = target.resolve(cls.containingPackage().name());
        Files.createDirectories(dir);

        Path file = dir.resolve(cls.name() + ".md");


        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write(frontMatter(cls));
            writer.newLine();
            writer.write(title(cls));
            writer.newLine();
            writer.write(description(cls));
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    private static String frontMatter(ClassDoc cls) {
        return String.format(
                "---" +
                "\n" +
                "title: %s" +
                "\n" +
                "---",
                cls.name());
    }

    private static String title(ClassDoc cls) {
        return String.format("# %s", cls.name());
    }

    private static String description(ClassDoc cls) {
        return cls.commentText();
    }


//            System.out.println("###");
//            System.out.println(cls.name());
////            System.out.println(cls.commentText());
//            System.out.println("###");
//
//    MethodDoc[] methods = cls.methods();
//            for(MethodDoc method : cls.methods())
//
//    {
//        System.out.println(method.commentText());
//        System.out.println(String.format("\n%s\n%s %s%s",
//                method.returnType(),
//                method.modifiers(),
//                method.name(),
//                method.signature()));
//
//        if (method.parameters().length > 0) {
//            System.out.println("params:");
//
//            for (Parameter p : method.parameters()) {
//                System.out.println(String.format("%s %s", p.name(), paramComment(method, p)));
//            }
//        }
//    }

    }

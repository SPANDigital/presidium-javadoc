package net.spandigital.presidium;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static net.spandigital.presidium.Markdown.link;
import static net.spandigital.presidium.Markdown.newLine;

/**
 *
 * @author Paco Mendes
 */
public class PackageWriter {

    public static void writeAll(Path target, RootDoc root) throws IOException {

        List<PackageDoc> packages = Arrays.stream(root.classes())
                .map(ClassDoc::containingPackage)
                .distinct()
                .sorted(Comparator.comparing(PackageDoc::name))
                .collect(Collectors.toList());

        Files.createDirectories(target);
        FileWriter.writeIndex(target, "Packages");

        writePackageList(target, packages);

        int i = 1;
        for (PackageDoc pkg : packages) {
            Path file = target.resolve(Markdown.fileName(i++, pkg.name()));
            writeArticle(file, pkg);
        }
    }

    private static void writePackageList(Path dir, List<PackageDoc> packages) {
        Path file = dir.resolve(Markdown.fileName(0, "package-summary"));
        FileWriter.write(file, Markdown.join(
                Markdown.frontMatter("Packages"),
                packageList(packages)
        ));
    }

    private static void writeArticle(Path file, PackageDoc pkg) {
        Comment comment = Comment.parse(pkg);
        FileWriter.write(file, Markdown.join(
                Markdown.frontMatter(pkg.name()),
                comment.getSummary(),
                packageClasses(pkg),
                Markdown.h1( "Package Description"),
                comment.getBody()
        ));
    }

    private static String packageList(List<PackageDoc> packages) {
        return packages.size() == 0 ? "" :
                Markdown.tableHeader("Package", "Description") +
                packages.stream()
                    .sorted()
                    .map(p -> Markdown.tableRow(Markdown.link(p.name(), p.name()), Comment.parse(p).getSummary()))
                    .collect(Collectors.joining()) +
                newLine();
    }

    private static String packageClasses(PackageDoc pkg) {
        return  classTable("Interfaces", pkg.interfaces()) +
                classTable("Enums", pkg.enums()) +
                classTable("Classes", pkg.ordinaryClasses()) +
                classTable("Exceptions", pkg.exceptions());
    }

    private static String classTable(String type, ClassDoc[] classes) {
        return classes.length == 0 ? "" :
                Markdown.h1(type) +
                Markdown.tableHeader(type, "Description") +
                Arrays.stream(classes).sorted()
                    .map(c -> Markdown.tableRow(
                            link("../classes#" + c.name().toLowerCase(), c.name()),
                            Comment.parse(c).getSummary()))
                    .collect(Collectors.joining()) +
                newLine();
    }


}

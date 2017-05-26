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

/**
 *
 * @author Paco Mendes
 */
public class PackageWriter {

    public static void write(Path target, RootDoc root) throws IOException {

        List<PackageDoc> packages = Arrays.stream(root.classes())
                .map(ClassDoc::containingPackage)
                .distinct()
                .sorted(Comparator.comparing(PackageDoc::name))
                .collect(Collectors.toList());

        Path dir = target.resolve("Packages");
        Files.createDirectories(dir);

        writePackageList(dir, packages);

        int i = 1;
        for (PackageDoc pkg : packages) {
            Path file = dir.resolve(Template.fileName(i++, pkg.name()));
            writeArticle(file, pkg);
        }
    }

    private static void writePackageList(Path dir, List<PackageDoc> packages) {
        Path file = dir.resolve(Template.fileName(0, "package-summary"));
        StringBuffer content = new StringBuffer();
        content.append(Template.frontMatter("Packages"));
        content.append(packageList(packages));
        FileWriter.write(file, content);
    }

    private static void writeArticle(Path file, PackageDoc pkg) {
        StringBuffer content = new StringBuffer();
        content.append(Template.frontMatter(pkg.name()));

        content.append(Template.firstLine(pkg));
        content.append("\n");
        content.append(packageClasses(pkg));
        content.append("\n");
        content.append(Template.h1("Package Description"));
        content.append(pkg.commentText());

        FileWriter.write(file, content);
    }

    private static String packageList(List<PackageDoc> packages) {
        return packages.size() > 0 ?
                        "| Package    | Description\n" +
                        "|:---------|:-----------\n" +
                                packages.stream()
                                .sorted()
                                .map(p -> String.format("|[%s](#%s) |%s", p.name(), p.name(), Template.firstLine(p)))
                                .collect(Collectors.joining("\n")) +
                        "\n"
                : "";


    }

    private static String packageClasses(PackageDoc pkg) {
        return  classTable("Interfaces", pkg.interfaces()) +
                classTable("Enums", pkg.enums()) +
                classTable("Classes", pkg.ordinaryClasses()) +
                classTable("Exceptions", pkg.exceptions());
    }

    private static String classTable(String type, ClassDoc[] classes) {
        return classes.length > 0 ?
                Template.h1(type) +
                "| "+ type +"    | Description\n" +
                "|:---------|:-----------\n" +
                Arrays.stream(classes).sorted()
                    .map(c -> String.format("|%s |%s", c.name(), Template.firstLine(c)))
                    .collect(Collectors.joining("\n")) +
                "\n"
                : "";
    }


}

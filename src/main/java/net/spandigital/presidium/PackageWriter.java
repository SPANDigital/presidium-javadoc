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

import static net.spandigital.presidium.Markdown.*;

/**
 * Generate package-level javadoc in markdown.
 *
 * @author Paco Mendes
 */
public class PackageWriter {

    private RootDoc root;
    private Path destination;
    private String sectionUrl;

    public static PackageWriter init(RootDoc root, Path destination, String sectionUrl) {
        PackageWriter writer = new PackageWriter();
        writer.root = root;
        writer.destination = destination;
        writer.sectionUrl = sectionUrl;
        return writer;
    }

    private PackageWriter() {}

    public void writeAll() throws IOException {

        List<PackageDoc> packages = Arrays.stream(root.classes())
                .map(ClassDoc::containingPackage)
                .distinct()
                .sorted(Comparator.comparing(PackageDoc::name))
                .collect(Collectors.toList());

        Files.createDirectories(destination);
        FileWriter.writeIndex(destination, "Packages");

        writePackageList(packages);

        int i = 1;
        for (PackageDoc pkg : packages) {
            String name = FileWriter.fileName(i++, pkg.name());
            Path file = destination.resolve(name);
            writeArticle(file, pkg);
        }
    }

    private void writePackageList(List<PackageDoc> packages) {
        String name = FileWriter.fileName(0, "package-summary");
        Path file = destination.resolve(name);
        FileWriter.write(file, Markdown.join(
                frontMatter("Packages"),
                packageList(packages)
        ));
    }

    private void writeArticle(Path file, PackageDoc pkg) {
        FileWriter.write(file, Markdown.join(
                frontMatter(pkg.name()),
                docSummary(pkg),
                packageClasses(pkg),
                h1( "Package Description"),
                docComment(pkg)
        ));
    }

    private static String packageList(List<PackageDoc> packages) {
        return packages.size() == 0 ? "" :
                Markdown.join(
                    tableHeader("Package", "Description"),
                    packages.stream()
                            .sorted()
                            .map(p -> tableRow(anchorLink(p.name(), p.name()), docSummary(p)))
                            .collect(Collectors.joining()),
                    newLine()
                );
    }

    private String packageClasses(PackageDoc pkg) {
        return  classTable("Interfaces", pkg.interfaces()) +
//                classTable("Enums", enums(pkg)) +
                classTable("Classes", pkg.ordinaryClasses()) +
                classTable("Exceptions", pkg.exceptions());
    }

    private String classTable(String type, ClassDoc[] classes) {
        return classes.length == 0 ? "" :
                Markdown.join(
                    h1(type),
                    tableHeader(type, "Description"),
                    Arrays.stream(classes)
                            .sorted()
                            .map(c -> tableRow(
                                    siteLink(c.name(), sectionUrl + "/classes#" + c.qualifiedName()),
                                    docSummary(c)))
                            .collect(Collectors.joining()),
                    newLine()
                );
    }

}

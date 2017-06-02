package net.spandigital.presidium;

import com.sun.javadoc.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.spandigital.presidium.Markdown.*;

/**
 * @author Paco Mendes
 */
public class ClassWriter {

    private RootDoc root;
    private Path destination;
    private String sectionUrl;
    private Set<String> knownQualifiers;

    public static ClassWriter init(RootDoc root, Path destination, String sectionUrl) {
        ClassWriter writer = new ClassWriter();
        writer.root = root;
        writer.destination = destination;
        writer.sectionUrl = sectionUrl;
        return writer;
    }

    public void writeAll() throws IOException {

        List<ClassDoc> classes = Arrays.stream(root.classes())
                .sorted(Comparator.comparing(ClassDoc::name))
                .collect(Collectors.toList());

        knownQualifiers = classes.stream()
                .map(ClassDoc::qualifiedName)
                .collect(Collectors.toSet());

        Files.createDirectories(destination);
        FileWriter.writeIndex(destination, "Classes");

        int i = 0;
        for (ClassDoc cls : classes) {
            Path file = destination.resolve(Markdown.fileName(i++, cls.name()));
            write(file, cls);
        }
    }

    public void write(Path file, ClassDoc cls) throws IOException {
        FileWriter.write(file, Markdown.join(
                Markdown.frontMatter(cls.name()),
                header(cls),
                Markdown.newLine(),
                Markdown.summary(cls),
                Markdown.newLine(),
                constructorList(cls),
                methodList(cls),
                classDescription(cls),
                constructorDetail(cls),
                methodDetail(cls)
        ));
    }

    private String header(ClassDoc cls) {
        String containingPackage = cls.containingPackage().name();
        return Markdown.join(
                Markdown.anchor(cls),
                Markdown.siteLink(containingPackage, sectionUrl + "/packages/#" + containingPackage),
                Markdown.h1(title(cls))
        );
    }

    private static String title(ClassDoc cls) {
        switch (classType(cls)) {
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

    private String constructorList(ClassDoc cls) {

        return cls.constructors().length == 0 ? "" :
                Markdown.h1("Constructor Summary") +
                        Markdown.tableHeader("Modifiers", "Constructor") +
                        Arrays.stream(cls.constructors())
                                .sorted()
                                .map(c -> Markdown.tableRow(
                                        c.modifiers(),
                                        this.methodLink(c)))
                                .collect(Collectors.joining()) +  newLine();
    }

    private String methodList(ClassDoc cls) {

        return cls.methods().length == 0 ? "" :
                Markdown.h1("Method Summary") +
                        Markdown.tableHeader("Modifiers", "Return", "Method") +
                        Arrays.stream(cls.methods())
                                .sorted()
                                .map(m -> Markdown.tableRow(
                                        m.modifiers(),
                                        this.typeLink(m.returnType()),
                                        this.methodLink(m)))
                                .collect(Collectors.joining()) +  newLine();
    }


    private String classDescription(ClassDoc cls) {
        String comment = Markdown.content(cls);
        return comment.length() == 0 ? ""  :
            Markdown.join(
                    Markdown.h1("Description"),
                    Markdown.content(cls)
            );
    }

    /**
     * Generates a link for a type if it's know
     * @param type
     * @return
     */
    private String typeLink(Type type) {
        return this.knownQualifiers.contains(type.qualifiedTypeName()) ?
                siteLink(type.typeName(), sectionUrl + "/classes#" + type.qualifiedTypeName()) :
                type.typeName();
    }

    private String methodLink(ExecutableMemberDoc method) {
        String params = Arrays.stream(method.parameters())
                .map(p ->  String.format("%s %s", typeLink(p.type()), p.name()))
                .collect(Collectors.joining(", "));
        return String.format("%s ( %s )", anchorLink(method.name(), method.qualifiedName()), params);
    }

    private String constructorDetail(ClassDoc cls) {
        return cls.constructors().length == 0 ? "" :
                Markdown.join(
                        Markdown.h1("Constructor Detail"),
                        Arrays.stream(cls.constructors())
                            .map(c -> memberDetail(c))
                            .collect(Collectors.joining(Markdown.newLine()))
                );

    }

    private String methodDetail(ClassDoc cls) {
        return cls.methods().length == 0 ? "" :
                Markdown.join(
                        Markdown.h1("Method Detail"),
                        Markdown.anchor(cls),
                        Arrays.stream(cls.methods())
                                .map(m -> memberDetail(m))
                                .collect(Collectors.joining(Markdown.newLine()))
                );

    }

    private String memberDetail(ExecutableMemberDoc member) {
        String params = Arrays.stream(member.parameters())
                .map(p ->  String.format("%s %s", typeLink(p.type()), p.name()))
                .collect(Collectors.joining(", "));
        String signature = String.format("%s ( %s )", member.name(), params);
        return Markdown.join(
            Markdown.hr(),
            Markdown.anchor(member),
            Markdown.h2(member.name()),
            Markdown.quote(signature),
            Markdown.content(member),
            Markdown.newLine()
        );
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

}

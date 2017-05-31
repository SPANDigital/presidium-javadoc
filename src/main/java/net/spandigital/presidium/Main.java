package net.spandigital.presidium;

import org.apache.commons.cli.*;

/**
 *
 */
public class Main {

    private static final String HELP = "h";
    private static final String TITLE = "t";
    private static final String URL = "u";
    private static final String SOURCE_PATH = "s";
    private static final String PACKAGES = "p";
    private static final String DIRECTORY = "d";

    public static void main(String[] args) throws Exception {

        CommandLine cmd = parse(args);
        if (cmd.hasOption("h")) {
            return;
        }
        com.sun.tools.javadoc.Main.execute("Presidium Doclet", Doclet.class.getCanonicalName(), new String[]{
                "-sourcepath", cmd.getOptionValue(SOURCE_PATH),
                "-subpackages", cmd.hasOption(PACKAGES) ? cmd.getOptionValue(PACKAGES) : ".",
                "-d", cmd.getOptionValue(DIRECTORY, "docs"),
                "-t", cmd.getOptionValue(TITLE, "javadoc"),
                "-u", cmd.getOptionValue(URL, "reference/javadoc")
        });
    }

    private static CommandLine parse(String[] args) {
        Options options = new Options();

        options.addOption(Option.builder(HELP)
                .longOpt("help")
                .desc("Shows this help.")
                .build());

        options.addOption(Option.builder(SOURCE_PATH)
                .longOpt("sourcepath")
                .numberOfArgs(1)
                .argName("path")
                .desc("Java source code directory.")
                .required()
                .build());

        options.addOption(Option.builder(PACKAGES)
                .longOpt("subpackages")
                .numberOfArgs(1)
                .argName("package1:package2:...")
                .desc("Packages to generate documentation from. default: all")
                .build());

        options.addOption(Option.builder(DIRECTORY)
                .longOpt("directory")
                .numberOfArgs(1)
                .argName("path")
                .desc("The destination directory to save the generated documentation to. default: './docs'")
                .build());

        options.addOption(Option.builder(TITLE)
                .longOpt("title")
                .numberOfArgs(1)
                .argName("string")
                .desc("Reference title. default: 'javadoc'")
                .build());

        options.addOption(Option.builder(URL)
                .longOpt("url")
                .numberOfArgs(1)
                .argName("foo/bar/{title-slug}")
                .desc("Section url. default: 'reference/javadoc'")
                .build());

        try {
            CommandLine cmd = new DefaultParser().parse(options, args);
            if (cmd.hasOption(HELP)) {
                printHelp(options);
            }
            return cmd;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp(options);
            System.exit(1);
            return null;
        }
    }

    private static void printHelp(Options options) {
        new HelpFormatter().printHelp("presidium-javadoc", options);
    }

}
package net.spandigital.presidium;

import org.apache.commons.cli.*;

/**
 *
 */
public class Main {

    private static final String SOURCE_PATH = "s";
    private static final String SUB_PACKAGES = "p";
    private static final String DIRECTORY = "d";

    public static void main(String[] args) throws Exception {

        CommandLine cmd = parse(args);
        com.sun.tools.javadoc.Main.execute("Presidium Doclet", Doclet.class.getCanonicalName(), new String[]{
                "-sourcepath", cmd.getOptionValue(SOURCE_PATH),
                "-subpackages", cmd.hasOption(SUB_PACKAGES) ? cmd.getOptionValue(SUB_PACKAGES) : ".",
                "-d", cmd.hasOption(DIRECTORY) ? cmd.getOptionValue(DIRECTORY) : "docs"
        });
    }

    private static CommandLine parse(String[] args) {
        Options options = new Options();

        options.addOption(Option.builder(SOURCE_PATH)
                .longOpt("sourcepath")
                .argName("path")
                .desc("Java source code directory.")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder(SUB_PACKAGES)
                .longOpt("subpackages")
                .argName("package1:package2:...")
                .desc("Packages to generate documentation from.")
                .numberOfArgs(1)
                .build());

        options.addOption(Option.builder(DIRECTORY)
                .longOpt("directory")
                .argName("path")
                .desc("The destination directory to save the generated documentation to.")
                .numberOfArgs(1)
                .build());

        try {
            return new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            new HelpFormatter().printHelp("presidium-javadoc", options);
            System.exit(1);
            return null;
        }
    }

}
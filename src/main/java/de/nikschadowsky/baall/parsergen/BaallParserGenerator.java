package de.nikschadowsky.baall.parsergen;

import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.grammar.generation.GrammarReader;
import de.nikschadowsky.baall.parsergen.grammar.optimization.GrammarMinimizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * File created on 24.02.2024
 */
public class BaallParserGenerator {

    private static final String helpText =
            """
                    BAALL Parser Gen

                    Description:
                    This console tool compiles grammar files into target code. Below are the available arguments and options:

                    Usage:
                    java GrammarCompiler <grammarLocation> [-d <targetDestination>] [-p <packageName>] [-m] [-o | -k]

                    Arguments:
                    - <grammarLocation>: Location of the grammar file.

                    Options:
                    - -d <targetDestination>: Specify the destination directory for the compiled code. Default is the current directory.
                    - -p <packageName>: Specify the package name for the compiled code. Default is "x.y.z".
                    - -m: Minimize the grammar. (Optional)
                    - -o: Override existing files. (Default)
                    - -k: Keep existing classes.

                    Example:
                    java GrammarCompiler grammar.txt -d outputDir -p com.example.grammar -m -k

                    Note:
                    - If no options are provided, the tool will compile the grammar file with default settings.
                    - Use -h option to display this help text.

                    Help:
                    java GrammarCompiler -h
                    """;
    private final Logger logger = LogManager.getLogger(BaallParserGenerator.class);


    public static void main(String[] args) {
        new BaallParserGenerator(args);
    }

    private BaallParserGenerator(String[] args) {
        logger.info("Starting BaallParserGenerator...");
        final List<String> arguments = Arrays.asList(args);

        // configuration
        String grammarLocation;
        String targetDestination = ".";
        String packageName = "x.y.z";
        boolean minimizeGrammar = false;
        boolean overrideExistingFiles = true;

        if (arguments.isEmpty()) {
            throw new IllegalArgumentException("Please provide the location of the grammar file");
        }
        if (arguments.get(0).equals("-h")) {
            logHelpText();
            System.exit(0);
        }

        grammarLocation = arguments.get(0);

        for (int i = 1; i < arguments.size(); i++) {
            switch (arguments.get(i)) {
                case "-d" -> targetDestination = arguments.get(++i);
                case "-p" -> packageName = arguments.get(++i);
                case "-m" -> minimizeGrammar = true;
                case "-o" -> overrideExistingFiles = true;
                case "-k" -> overrideExistingFiles = false;
                default -> throw new IllegalArgumentException("Unknown option: " + arguments.get(i));
            }
        }

        try {
            Grammar grammar = GrammarReader.getInstance().generateGrammar(Path.of(grammarLocation));
            logger.info("Grammar successfully generated!");

            if (minimizeGrammar) {
                grammar = new GrammarMinimizer().minimizeGrammar(grammar);
            }
            logger.info("Grammar minimized!");

            Exporter.getInstance()
                    .exportClasses(grammar, Path.of(targetDestination), packageName, overrideExistingFiles);

            logger.info("Classes successfully generated!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void logHelpText() {
        logger.info("\n" + helpText);
        System.exit(0);
    }
}

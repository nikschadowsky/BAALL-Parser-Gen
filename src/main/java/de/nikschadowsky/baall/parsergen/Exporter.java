package de.nikschadowsky.baall.parsergen;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import de.nikschadowsky.baall.parsergen.generator.ParserClassGenerator;
import de.nikschadowsky.baall.parsergen.generator.TerminalComparableInterfaceGenerator;
import de.nikschadowsky.baall.parsergen.generator.TerminalTypeEnumGenerator;
import de.nikschadowsky.baall.parsergen.grammar.Grammar;
import de.nikschadowsky.baall.parsergen.util.FileUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * File created on 03.04.2024
 */
public class Exporter {

    private static final Logger logger = LogManager.getLogger(Exporter.class);

    private static Exporter instance;

    public static Exporter getInstance() {
        if (instance == null) {
            instance = new Exporter();
        }
        return instance;
    }

    private Exporter() {
    }

    public void exportClasses(Grammar grammar, Path targetDestination, String packageName, boolean overrideExisting) {
        List<TypeSpec> toExport = new ArrayList<>();

        toExport.add(ParserClassGenerator.getInstance().generateTypeSpec(grammar));
        logger.info("Parser class successfully generated");
        toExport.add(TerminalComparableInterfaceGenerator.getInstance().generateTypeSpec(grammar));
        logger.info("TerminalComparable interface successfully generated");
        toExport.add(TerminalTypeEnumGenerator.getInstance().generateTypeSpec(grammar));
        logger.info("TerminalType enum successfully generated");

        try {
            Path targetDirectory = targetDestination.resolve("generator_output");
            if (!Files.exists(targetDirectory)) {
                Files.createDirectory(targetDirectory);
            }

            for (TypeSpec ts : toExport) {
                String javaFileContent = JavaFile.builder(packageName, ts).skipJavaLangImports(true).build().toString();
                FileUtility.writeToFile(targetDirectory.resolve(ts.name + ".java"), javaFileContent, overrideExisting);
            }
        } catch (IOException e) {
            logger.fatal("Files could not be exported!: ", e);
            // unrecoverable fatal error
            System.exit(1);
        }

        logger.info("Successfully exported all classes!");
    }

}

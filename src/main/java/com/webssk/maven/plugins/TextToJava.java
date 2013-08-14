package com.webssk.maven.plugins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextToJava implements Runnable {
    private static final String PACKAGE_NAME = "package.name";
    private static final String CLASS_NAME = "class.name";
    private static final String TEXT_STRING = "text.string";
    private static final String INPUT_FILE = "input.file";
    private static final String OUTPUT_FILE = "output.file";
    private final Path inputPath;
    private final Path outputPath;
    private final Properties params = new Properties();
    private final StringBuffer script = new StringBuffer(getScript());

    public TextToJava(Path inputFile, Path outputFile) {
        this.inputPath = inputFile;
        this.outputPath = outputFile;
        params.setProperty(INPUT_FILE, inputFile.toString());
        params.setProperty(OUTPUT_FILE, outputFile.toString());
    }

    public void setClassName(String className) {
        params.setProperty(CLASS_NAME, className);
    }

    public void setPackageName(String packageName) {
        params.setProperty(PACKAGE_NAME, packageName);
    }

    @Override
    public String toString() {
        return script != null ? script.toString() : "";
    }

    static class NewFileVisitor extends SimpleFileVisitor<Path> {
        private final Path outputPath;
        private final PathMatcher matcher;
        private final List<Path> files;

        public NewFileVisitor(Path outputPath, String pattern, List<Path> files) {
            FileSystem fileSystem = FileSystems.getDefault();
            String fullPattern = pattern;
            if (!pattern.startsWith("glob:") && !pattern.startsWith("regex:")) {
                fullPattern = "glob:" + pattern;
            }
            this.outputPath = outputPath;
            this.matcher = fileSystem.getPathMatcher(fullPattern);
            this.files = files;
        }

        @Override
        public FileVisitResult visitFile(Path inputFile,
                BasicFileAttributes inputAttrs) {
            if (matcher.matches(inputFile.getFileName())) {
                boolean needUpdate = true;
                try {
                    Path outputFile = getOutputFile(inputFile, outputPath);
                    if (Files.exists(outputFile)) {
                        BasicFileAttributes outputAttrs = Files.readAttributes(
                                outputFile, BasicFileAttributes.class);
                        needUpdate = inputAttrs.lastModifiedTime().compareTo(
                                outputAttrs.lastModifiedTime()) > 0;
                    }
                    if (needUpdate) {
                        files.add(inputFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return FileVisitResult.CONTINUE;
        }
    }

    public static List<Path> findFiles(Path inputPath, Path outputPath,
            String pattern) {
        List<Path> files = new ArrayList<>();
        try {
            Files.walkFileTree(inputPath, new NewFileVisitor(outputPath,
                    pattern, files));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        String inputPath = "src/main/sql";
        String pattern = "*.sql";
        String outputPath = "src/main/java/com/webssk/preparation/sql";
        String packageName = "com.webssk.preparation.sql";

        for (Path inputFile : findFiles(Paths.get(inputPath),
                Paths.get(outputPath), pattern)) {
            String fileName = inputFile.getFileName().toString();
            String className = fileName.substring(0, fileName.indexOf('.'));
            Path outputFile = getOutputFile(inputFile, Paths.get(outputPath));

            TextToJava text = new TextToJava(inputFile, outputFile);
            text.setPackageName(packageName);
            text.setClassName(className);
            pool.execute(text);
        }

        pool.shutdown();
    }

    public static Path getOutputFile(Path inputFile, Path outputPath) {
        String inputName = inputFile.getFileName().toString();
        String outputName = inputName.substring(0, inputName.indexOf('.'))
                + ".java";
        return Paths.get(outputPath.toString(), outputName);
    }

    private String quoteString(String string) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"");
        buffer.append(string.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\\\"").replaceAll("\n", "\\\\n"));
        buffer.append("\"");
        return buffer.toString();
    }

    private StringBuffer getText() {
        StringBuffer text = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader(
                inputPath.toFile()))) {
            int lineNo = 0;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (++lineNo > 1) {
                    text.append(" +\n");
                }
                text.append(quoteString(line + "\n"));
            }
        } catch (IOException e) {

        }
        return text;
    }

    private static StringBuffer getScript() {
        StringBuffer sb = new StringBuffer();
        sb.append("// This is autogenerated file, don't change it manually\n");
        sb.append("// Source file has located at @" + INPUT_FILE + "@\n");
        sb.append("package @" + PACKAGE_NAME + "@;\n");
        sb.append("\n");
        sb.append("public final class @" + CLASS_NAME + "@ {\n");
        sb.append("    public static final String TEXT =\n");
        sb.append("@" + TEXT_STRING + "@;\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public String toString() {\n");
        sb.append("        return TEXT;\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb;
    }

    @Override
    public void run() {
        params.setProperty(TEXT_STRING, getText().toString());
        Pattern pattern = Pattern.compile("@[^@]+@");
        Matcher matcher = pattern.matcher(script);
        while (matcher.find()) {
            String paramName = script.substring(matcher.start() + 1,
                    matcher.end() - 1);
            if (params.containsKey(paramName)) {
                script.replace(matcher.start(), matcher.end(),
                        params.getProperty(paramName));
            } else {
                script.replace(matcher.start(), matcher.end(), "");
            }
        }

        try (Writer writer = new BufferedWriter(new FileWriter(
                outputPath.toString()))) {
            writer.write(script.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

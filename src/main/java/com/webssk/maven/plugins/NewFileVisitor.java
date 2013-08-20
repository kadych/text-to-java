package com.webssk.maven.plugins;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class NewFileVisitor extends SimpleFileVisitor<Path> {
    private final Path outputPath;
    private final PathMatcher matcher;
    private final String ext;
    private final Map<Path, Path> files;

    public NewFileVisitor(Path outputPath, String pattern, String ext,
            Map<Path, Path> files) {
        FileSystem fileSystem = FileSystems.getDefault();
        String fullPattern = pattern;
        if (!pattern.startsWith("glob:") && !pattern.startsWith("regex:")) {
            fullPattern = "glob:" + pattern;
        }
        this.outputPath = outputPath;
        this.ext = ext;
        this.matcher = fileSystem.getPathMatcher(fullPattern);
        this.files = files;
    }

    private Path getOutputFile(Path inputFile, Path outputPath, String ext) {
        String inputName = inputFile.getFileName().toString();
        String outputName = inputName.substring(0, inputName.indexOf('.'))
                + ext;
        return Paths.get(outputPath.toString(), outputName);
    }

    @Override
    public FileVisitResult visitFile(Path inputFile,
            BasicFileAttributes inputAttrs) {
        if (matcher.matches(inputFile.getFileName())) {
            boolean needUpdate = true;
            try {
                Path outputFile = getOutputFile(inputFile, outputPath, ext);
                if (Files.exists(outputFile)) {
                    BasicFileAttributes outputAttrs = Files.readAttributes(
                            outputFile, BasicFileAttributes.class);
                    needUpdate = inputAttrs.lastModifiedTime().compareTo(
                            outputAttrs.lastModifiedTime()) > 0;
                }
                if (needUpdate) {
                    files.put(inputFile, outputFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FileVisitResult.CONTINUE;
    }

    public static Map<Path, Path> findFiles(Path inputPath, Path outputPath,
            String pattern, String ext) {
        Map<Path, Path> files = new HashMap<>();
        try {
            Files.walkFileTree(inputPath, new NewFileVisitor(outputPath,
                    pattern, ext, files));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}
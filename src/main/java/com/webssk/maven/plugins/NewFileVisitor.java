package com.webssk.maven.plugins;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class NewFileVisitor extends SimpleFileVisitor<Path> {
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
                Path outputFile = null; // getOutputFile(inputFile,
                                        // outputPath);
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
}
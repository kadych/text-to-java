package com.webssk.maven.plugins;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

public abstract class TextToCodeMojo extends AbstractMojo {

    @Parameter(alias = "input.path", defaultValue = ".")
    protected String inputPath;

    @Parameter(alias = "output.path", defaultValue = ".")
    protected String outputPath;

    @Parameter(defaultValue = "*.txt")
    protected String pattern;

    @Parameter(alias = TextToCodeProperties.INPUT_CHARSET)
    protected String inputCharset;

    @Parameter(alias = TextToCodeProperties.OUTPUT_CHARSET)
    protected String outputCharset;

    public Map<Path, Path> findFiles(String ext) {
        return NewFileVisitor.findFiles(Paths.get(inputPath),
                Paths.get(outputPath), pattern, ext);
    }
}

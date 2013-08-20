package com.webssk.maven.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

public class TextToCodeMojo extends AbstractMojo {

    @Parameter(alias = "input.path", defaultValue = ".")
    protected String inputPath;

    @Parameter(alias = "output.path", defaultValue = ".")
    protected String outputPath;

    @Parameter(defaultValue = "*.txt")
    protected String pattern;

    @Parameter(alias = "input.charset")
    protected String inputCharset;

    @Parameter(alias = "output.charset")
    protected String outputCharset;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("input.path = " + inputPath);
        getLog().info("output.path = " + outputPath);
        getLog().info("pattern = " + pattern);
    }
}

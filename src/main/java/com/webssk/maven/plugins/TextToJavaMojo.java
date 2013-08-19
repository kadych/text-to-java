package com.webssk.maven.plugins;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TextToJavaMojo extends AbstractMojo {
    @Parameter(alias = "package.name", defaultValue = "")
    private String packageName;

    @Parameter(alias = "input.path", defaultValue = ".")
    private String inputPath;

    @Parameter(alias = "output.path", defaultValue = ".")
    private String outputPath;

    @Parameter(defaultValue = "*.txt")
    private String pattern;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("package.name = " + packageName);
        getLog().info("input.path = " + inputPath);
        getLog().info("output.path = " + outputPath);
        getLog().info("pattern = " + pattern);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        // for (Path inputFile : TextToCode.findFiles(Paths.get(inputPath),
        // Paths.get(outputPath), pattern)) {
        // String inputName = inputFile.getFileName().toString();
        // Path outputFile = TextToCode.getOutputFile(inputFile,
        // Paths.get(outputPath));
        // String outputName = outputFile.getFileName().toString();
        //
        // getLog().info(inputName + " -> " + outputName);
        //
        // TextToCode text = new TextToJava();
        // text.setInputPath(inputFile);
        // text.setOutputPath(outputFile);
        // text.setPackageName(packageName);
        // text.setClassName(inputName.substring(0, inputName.indexOf('.')));
        //
        // pool.execute(text);
        // }

        pool.shutdown();
    }
}

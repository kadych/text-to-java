package com.webssk.maven.plugins;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "python", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TextToPythonMojo extends TextToCodeMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (Map.Entry<Path, Path> pair : findFiles(".py").entrySet()) {
            String inputPath = pair.getKey().toString();
            String outputPath = pair.getValue().toString();
            String outputFile = pair.getValue().getFileName().toString();
            String variableName = outputFile.substring(0,
                    outputFile.indexOf('.'));

            getLog().info(inputPath + " -> " + outputPath);

            TextToCode text = new TextToPython();
            TextToPythonProperties params = (TextToPythonProperties) text
                    .getProperties();
            params.setInputFile(inputPath);
            params.setOutputFile(outputPath);
            params.setInputCharset(inputCharset);
            params.setOutputCharset(outputCharset);
            params.setVariableName(variableName);
            pool.execute(text);
        }

        pool.shutdown();
    }
}

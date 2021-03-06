package com.webssk.maven.plugins;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "java", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TextToJavaMojo extends TextToCodeMojo {

    @Parameter(alias = TextToJavaProperties.PACKAGE_NAME, defaultValue = "")
    protected String packageName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (Map.Entry<Path, Path> pair : findFiles(".java").entrySet()) {
            String inputPath = pair.getKey().toString();
            String outputPath = pair.getValue().toString();
            String outputFile = pair.getValue().getFileName().toString();
            String className = outputFile.substring(0, outputFile.indexOf('.'));

            getLog().info(inputPath + " -> " + outputPath);

            TextToCode text = new TextToJava();
            TextToJavaProperties params = (TextToJavaProperties) text
                    .getProperties();
            params.setInputFile(inputPath);
            params.setOutputFile(outputPath);
            params.setPackageName(packageName);
            params.setClassName(className);
            params.setInputCharset(inputCharset);
            params.setOutputCharset(outputCharset);
            pool.execute(text);
        }

        pool.shutdown();
    }
}

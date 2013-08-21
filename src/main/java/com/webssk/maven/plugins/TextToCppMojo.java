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

@Mojo(name = "cpp", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class TextToCppMojo extends TextToCodeMojo {

    @Parameter(alias = TextToCppProperties.STDAFX_PROLOG, defaultValue = "")
    protected String stdafxProlog;

    @Parameter(alias = TextToCppProperties.UTF8_BOM, defaultValue = "")
    protected String utf8Bom;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        for (Map.Entry<Path, Path> pair : findFiles(".cpp").entrySet()) {
            String inputPath = pair.getKey().toString();
            String outputPath = pair.getValue().toString();
            String outputFile = pair.getValue().getFileName().toString();
            String variableName = outputFile.substring(0,
                    outputFile.indexOf('.'));

            getLog().info(inputPath + " -> " + outputPath);

            TextToCode text = new TextToCpp();
            TextToCppProperties params = (TextToCppProperties) text
                    .getProperties();
            params.setInputFile(inputPath);
            params.setOutputFile(outputPath);
            params.setInputCharset(inputCharset);
            params.setOutputCharset(outputCharset);
            params.setVariableName(variableName);
            params.setStdafxProlog(stdafxProlog);
            params.setUtf8Bom(utf8Bom);
            pool.execute(text);
        }

        pool.shutdown();
    }
}

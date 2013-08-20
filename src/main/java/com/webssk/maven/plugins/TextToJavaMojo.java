package com.webssk.maven.plugins;

import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Parameter(alias = "package.name", defaultValue = "")
    protected String packageName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        if (!packageName.isEmpty()) {
            getLog().info("package.name = " + packageName);
        }

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Map<Path, Path> files = NewFileVisitor.findFiles(Paths.get(inputPath),
                Paths.get(outputPath), pattern, ".java");
        for (Map.Entry<Path, Path> pair : files.entrySet()) {
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

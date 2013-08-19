package com.webssk.maven.plugins;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TextToCode implements Runnable {
    protected TextToCodeProperties params;
    private StringBuffer script;

    public TextToCode() {
        try {
            this.params = getPropertiesClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            this.params = new TextToCodeProperties();
        }
        this.script = new StringBuffer(getScript());
    }

    protected Class<? extends TextToCodeProperties> getPropertiesClass() {
        return TextToCodeProperties.class;
    }

    public TextToCodeProperties getProperties() {
        return params;
    }

    public static void main(String[] args) throws InstantiationException,
            IllegalAccessException {
        TextToCpp.main(args);
    }

    protected Path getOutputFile(Path inputFile, Path outputPath) {
        String inputName = inputFile.getFileName().toString();
        String outputName = inputName.substring(0, inputName.indexOf('.'))
                + getExt();
        return Paths.get(outputPath.toString(), outputName);
    }

    protected String quoteString(String string) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"");
        buffer.append(string.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\\\"").replaceAll("\n", "\\\\n"));
        buffer.append("\"");
        return buffer.toString();
    }

    protected String stringConcat() {
        return "\n";
    }

    protected StringBuffer getText() {
        StringBuffer text = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new FileReader(
                params.getInputFile()))) {
            int lineNo = 0;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (++lineNo > 1) {
                    text.append(stringConcat());
                }
                text.append(quoteString(line + "\n"));
            }
        } catch (IOException e) {

        }
        return text;
    }

    protected static String makeProperty(String name) {
        return String.format("${%s}", name);
    }

    protected abstract StringBuffer getScript();

    protected abstract String getExt();

    @Override
    public void run() {
        params.setTextString(getText().toString());
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(script);
        int pos = 0;
        while (matcher.find(pos)) {
            String paramName = matcher.group(1);
            System.out.println(paramName);
            if (params.containsKey(paramName)) {
                script.replace(matcher.start(), matcher.end(),
                        params.getProperty(paramName));
            } else {
                script.replace(matcher.start(), matcher.end(), "");
            }
            pos = matcher.start();
        }

        try (Writer writer = new BufferedWriter(new FileWriter(
                params.getOutputFile()))) {
            writer.write(script.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

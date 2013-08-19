package com.webssk.maven.plugins;

import java.util.Properties;

public class TextToCodeProperties {
    public static final String TEXT_STRING = "text.string";
    public static final String INPUT_FILE = "input.file";
    public static final String OUTPUT_FILE = "output.file";

    private final Properties params;

    public TextToCodeProperties() {
        this.params = new Properties();
    }

    public String getProperty(String key) {
        return params.getProperty(key, "");
    }

    public void setProperty(String key, String value) {
        params.setProperty(key, value);
    }

    public boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public void setInputFile(String inputFile) {
        setProperty(INPUT_FILE, inputFile);
    }

    public String getInputFile() {
        return getProperty(INPUT_FILE);
    }

    public void setOutputFile(String outputFile) {
        setProperty(OUTPUT_FILE, outputFile);
    }

    public String getOutputFile() {
        return getProperty(OUTPUT_FILE);
    }

    public void setTextString(String textString) {
        setProperty(TEXT_STRING, textString);
    }
}

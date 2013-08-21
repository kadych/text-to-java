package com.webssk.maven.plugins;

import java.util.Properties;

public class TextToCodeProperties {
    public static final String TEXT_STRING = "text.string";
    public static final String INPUT_FILE = "input.file";
    public static final String OUTPUT_FILE = "output.file";
    public static final String INPUT_CHARSET = "input.charset";
    public static final String OUTPUT_CHARSET = "output.charset";
    private static final String DEFAULT_CHARSET = "ISO-8859-1";
    private final Properties params = new Properties();

    public String getExt() {
        return ".txt";
    }

    public String getProperty(String key, String defaultValue) {
        return params.getProperty(key, defaultValue);
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

    public String getTextString() {
        return getProperty(TEXT_STRING);
    }

    public void setInputCharset(String charset) {
        setProperty(INPUT_CHARSET, charset);
    }

    public String getInputCharset() {
        return getProperty(INPUT_CHARSET, DEFAULT_CHARSET);
    }

    public void setOutputCharset(String charset) {
        setProperty(OUTPUT_CHARSET, charset);
    }

    public String getOutputCharset() {
        return getProperty(OUTPUT_CHARSET, DEFAULT_CHARSET);
    }
}

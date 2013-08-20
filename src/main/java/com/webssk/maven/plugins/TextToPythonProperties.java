package com.webssk.maven.plugins;

public class TextToPythonProperties extends TextToCodeProperties {
    public static final String VARIABLE_NAME = "variable.name";

    @Override
    public String getExt() {
        return ".py";
    }

    public void setVariableName(String variableName) {
        setProperty(VARIABLE_NAME, variableName);
    }

    public String getVariableName() {
        return getProperty(VARIABLE_NAME);
    }
}

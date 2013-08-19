package com.webssk.maven.plugins;

public class TextToPythonProperties extends TextToCodeProperties {
    public static final String VARIABLE_NAME = "variable.name";

    public void setVariableName(String variableName) {
        setProperty(VARIABLE_NAME, variableName);
    }
}

package com.webssk.maven.plugins;

public class TextToCppProperties extends TextToCodeProperties {
    public static final String VARIABLE_NAME = "variable.name";
    public static final String STDAFX_PROLOG = "stdafx.prolog";

    public void setVariableName(String variableName) {
        setProperty(VARIABLE_NAME, variableName);
    }

    public void setStdafxProlog(String stdafxProlog) {
        setProperty(STDAFX_PROLOG, stdafxProlog);
    }
}

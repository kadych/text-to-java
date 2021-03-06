package com.webssk.maven.plugins;

public class TextToCppProperties extends TextToCodeProperties {
    public static final String VARIABLE_NAME = "variable.name";
    public static final String STDAFX_PROLOG = "stdafx.prolog";
    public static final String UTF8_BOM = "utf8.bom";

    public void setVariableName(String variableName) {
        setProperty(VARIABLE_NAME, variableName);
    }

    @Override
    public String getExt() {
        return ".cpp";
    }

    public String getVariableName() {
        return getProperty(VARIABLE_NAME);
    }

    public void setStdafxProlog(String stdafxProlog) {
        setProperty(STDAFX_PROLOG, stdafxProlog);
    }

    public String getStdafxProlog() {
        return getProperty(STDAFX_PROLOG);
    }

    public void setUtf8Bom(String bom) {
        setProperty(UTF8_BOM, bom);
    }

    public String getUtf8Bom() {
        return getProperty(UTF8_BOM);
    }
}

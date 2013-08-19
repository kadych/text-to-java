package com.webssk.maven.plugins;

public class TextToJavaProperties extends TextToCodeProperties {
    public static final String PACKAGE_NAME = "package.name";
    public static final String CLASS_NAME = "class.name";

    public void setPackageName(String packageName) {
        setProperty(TextToJavaProperties.PACKAGE_NAME, packageName);
    }

    public void setClassName(String className) {
        setProperty(TextToJavaProperties.CLASS_NAME, className);
    }
}

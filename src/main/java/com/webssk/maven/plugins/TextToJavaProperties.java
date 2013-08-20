package com.webssk.maven.plugins;

public class TextToJavaProperties extends TextToCodeProperties {
    public static final String PACKAGE_NAME = "package.name";
    public static final String CLASS_NAME = "class.name";

    @Override
    public String getExt() {
        return ".java";
    }

    public void setPackageName(String packageName) {
        setProperty(PACKAGE_NAME, packageName);
    }

    public String getPackageName() {
        return getProperty(PACKAGE_NAME);
    }

    public void setClassName(String className) {
        setProperty(CLASS_NAME, className);
    }

    public String getClassName() {
        return getProperty(CLASS_NAME);
    }
}

package com.webssk.maven.plugins;

public class TextToJava extends TextToCode {

    @Override
    protected StringBuffer getScript() {
        TextToJavaProperties javaParams = (TextToJavaProperties) getProperties();
        StringBuffer sb = new StringBuffer();
        sb.append("// This is autogenerated file, don't change it manually\n");
        sb.append("// Source file has located at "
                + makeProperty(TextToJavaProperties.INPUT_FILE) + "\n");
        if (!javaParams.getPackageName().isEmpty()) {
            sb.append("package "
                    + makeProperty(TextToJavaProperties.PACKAGE_NAME) + ";\n");
        }
        sb.append("\n");
        sb.append("public final class "
                + makeProperty(TextToJavaProperties.CLASS_NAME) + " {\n\n");
        sb.append("    public static final String TEXT =\n        ");
        sb.append(makeProperty(TextToJavaProperties.TEXT_STRING) + ";\n");
        sb.append("\n");
        sb.append("    @Override\n");
        sb.append("    public String toString() {\n");
        sb.append("        return TEXT;\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb;
    }

    @Override
    protected String stringConcat() {
        return " +\n        ";
    }

    @Override
    protected Class<? extends TextToCodeProperties> getPropertiesClass() {
        return TextToJavaProperties.class;
    }

    public static void main(String[] args) {
        TextToJava java = new TextToJava();
        TextToJavaProperties params = (TextToJavaProperties) java
                .getProperties();
        params.setInputFile("src/test/resources/test.sql");
        params.setInputCharset("windows-1251");
        params.setOutputFile("src/test/resources/test.java");
        params.setOutputCharset("utf-8");
        // params.setPackageName("com.webssk.preparation.sql");
        params.setClassName("Test");
        java.run();
    }
}

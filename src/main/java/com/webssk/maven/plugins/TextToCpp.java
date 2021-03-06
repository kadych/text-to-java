package com.webssk.maven.plugins;

public class TextToCpp extends TextToCode {

    @Override
    protected StringBuffer getScript() {
        TextToCppProperties cppParams = (TextToCppProperties) getProperties();
        StringBuffer sb = new StringBuffer();
        if (!cppParams.getUtf8Bom().isEmpty()) {
            sb.append("\uFEFF");
        }
        sb.append("// This is autogenerated file, don't change it manually\n");
        sb.append("// Source file has located at "
                + makeProperty(TextToCppProperties.INPUT_FILE) + "\n");
        if (!cppParams.getStdafxProlog().isEmpty()) {
            sb.append("#include \"StdAfx.h\"\n");
        }
        sb.append("\n");
        sb.append("const char* "
                + makeProperty(TextToCppProperties.VARIABLE_NAME) + " = \n    ");
        sb.append(makeProperty(TextToCppProperties.TEXT_STRING) + ";\n");
        if (!cppParams.getOutputCharset().isEmpty()) {
            sb.append("\n// vim: set fenc=${output.charset} ff=unix\n");
        }
        return sb;
    }

    @Override
    protected String stringConcat() {
        return "\n    ";
    }

    @Override
    protected Class<? extends TextToCodeProperties> getPropertiesClass() {
        return TextToCppProperties.class;
    }

    public static void main(String[] args) {
        TextToCpp cpp = new TextToCpp();
        TextToCppProperties params = (TextToCppProperties) cpp.getProperties();
        params.setInputFile("src/test/resources/test.sql");
        params.setInputCharset("windows-1251");
        params.setOutputFile("src/test/resources/test.cpp");
        params.setOutputCharset("utf-8");
        params.setStdafxProlog("1");
        params.setVariableName("test");
        params.setUtf8Bom("1");
        cpp.run();
    }
}

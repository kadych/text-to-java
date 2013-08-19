package com.webssk.maven.plugins;

import java.util.HashMap;
import java.util.Map;

public class TextToCodeFactory {
    private static Map<TextToCodeEnum, Class<? extends TextToCode>> factory = null;

    private TextToCodeFactory() {
    }

    public static Class<? extends TextToCode> getClass(String name) {
        if (factory == null) {
            factory = new HashMap<>();
            factory.put(TextToCodeEnum.JAVA, TextToJava.class);
            factory.put(TextToCodeEnum.PYTHON, TextToPython.class);
            factory.put(TextToCodeEnum.CPP, TextToCpp.class);
        }
        try {
            TextToCodeEnum value = TextToCodeEnum.valueOf(name.toUpperCase());
            if (factory.containsKey(value)) {
                return factory.get(value);
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static TextToCode create(String name) {
        Class<? extends TextToCode> class1 = getClass(name);
        try {
            if (class1 != null) {
                return class1.newInstance();
            } else {
                return null;
            }
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(TextToCodeFactory.getClass("python"));
    }
}

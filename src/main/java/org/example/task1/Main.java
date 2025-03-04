package org.example.task1;

import org.example.task1.engine.TestRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        TestRunner.runTests(ObservedClass.class);

    }
}



package org.example.task1.engine;

import java.lang.reflect.InvocationTargetException;

public class TestRunner {

    static public void runTests(Class clazz) throws InvocationTargetException, IllegalAccessException {
        var engine = new TestEngine(clazz);

        engine.runTests();
    }
}

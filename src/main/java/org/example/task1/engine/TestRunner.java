package org.example.task1.engine;

import org.example.task1.ObservedClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {

    static public void runTests(Class clazz) throws InvocationTargetException, IllegalAccessException {
        Method[] methodsList = clazz.getDeclaredMethods();
        orderMethods(methodsList);
    }

    void validate() {

    }

    static void orderMethods(Method[] methodsList) throws InvocationTargetException, IllegalAccessException {
        for (Method met:
             methodsList) {
            System.out.println(met.getName());
            met.setAccessible(true);
            met.invoke(new ObservedClass());
        }
    }

    void invokeMethods() {

    }

}

package org.example.task1.engine;

import org.example.task1.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TestEngine {

    private final Class inspected;
    private List<Method> orderedTests;
    private List<Method> beforeTest;
    private List<Method> afterTest;
    private Optional<Method> beforeSuite;
    private Optional<Method> afterSuite;
    private String errorString;

    TestEngine(Class clazz) throws RuntimeException {
        this.inspected = clazz;
        if (!validateMethods(clazz.getDeclaredMethods())) {
            throw new RuntimeException(errorString);
        }

        beforeTest = collectEach(clazz.getDeclaredMethods(), BeforeTest.class);
        afterTest = collectEach(clazz.getDeclaredMethods(), AfterTest.class);
        orderedTests = collectTests(clazz.getDeclaredMethods());
    }

    private static Object convert(String from) {
        if (from.matches("-?\\d+")) {
            return Integer.parseInt(from);
        } else if (from.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(from);
        } else if (from.matches("true|false|0|1")) {
            return Boolean.parseBoolean(from);
        } else return from;
    }

    void runTests() {

        Object observedObject;

        try {
            observedObject = inspected.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | RuntimeException |
                 NoSuchMethodException e) {
            System.out.println("Ошибка создания объекта");
            return;
        }
        beforeSuite.ifPresent(x -> callMeth(x, observedObject));

        runAllTests(observedObject);

        afterSuite.ifPresent(x -> callMeth(x, observedObject));
    }

    private void runAllTests(Object obj) {
        orderedTests.forEach(test -> {
            beforeTest.forEach(x -> callMeth(x, obj));
            callMeth(test, obj);
            afterTest.forEach(x -> callMeth(x, obj));
        });
    }

    private void callMeth(Method meth, Object obj) {
        meth.setAccessible(true);
        try {
            if (meth.isAnnotationPresent(CsvSource.class)) {
                String[] tmpArgs = meth.getAnnotation(CsvSource.class).value().split(",");
                Object[] args = Arrays.stream(tmpArgs).map(TestEngine::convert).toArray();
                meth.invoke(obj, args);
            } else {
                meth.invoke(obj);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        meth.setAccessible(false);
    }

    private boolean validateMethods(Method[] methods) {
        return addSuiteMethods(methods, BeforeSuite.class) && addSuiteMethods(methods, AfterSuite.class);
    }

    private boolean addSuiteMethods(Method[] methods, Class clazz) {
        var count = Arrays.stream(methods)
                .filter(x -> x.isAnnotationPresent(clazz))
                .filter(this::checkModifiers)
                .count();

        if (count > 1) {
            errorString = "Больше одного метода с аннотацией " + clazz.getName();
        }

        return errorString == null || errorString.isEmpty();
    }

    private boolean checkModifiers(Method meth) {
        var result = Modifier.isStatic(meth.getModifiers());
        if (!result) {
            errorString = "Метод " + meth.getName() + " должен быть static";
        }

        if (meth.isAnnotationPresent(AfterSuite.class)) {
            afterSuite = Optional.of(meth);
        } else {
            beforeSuite = Optional.of(meth);
        }
        return result;
    }


    private List<Method> collectEach(Method[] methods, Class clazz) {
        return Arrays.stream(methods)
                .filter(x -> x.isAnnotationPresent(clazz))
                .toList();
    }

    private List<Method> collectTests(Method[] methods) {
        return Arrays.stream(methods)
                .filter(x -> x.isAnnotationPresent(Test.class))
                .sorted(Comparator.comparingInt(x -> x.getAnnotation(Test.class).value()))
                .toList();
    }
}

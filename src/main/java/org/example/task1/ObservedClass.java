package org.example.task1;

import org.example.task1.annotation.BeforeSuite;

public class ObservedClass {

    @BeforeSuite
    void callBeforeSuite() {
        System.out.println("before suite method");
    }
}

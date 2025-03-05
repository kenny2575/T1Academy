package org.example.task1;

import org.example.task1.annotation.*;

import java.sql.SQLOutput;

public class ObservedClass {

    @BeforeSuite
    static void callBeforeSuite() {
        System.out.println("before suite method");
    }

    @AfterSuite
    static void callAfterSuite() {
        System.out.println("after suite method");
    }

    @BeforeTest
    void beforeTest() {
        System.out.println("Before test");
    }

    @AfterTest
    void afterTest(){
        System.out.println("After test");
    }

    @Test(2)
    void testPriorityTwo(){
        System.out.println("Test priority 2");
    }

    @Test
    void testDefaultPriority() {
        System.out.println("Default priority");
    }

    @Test(5)
    void testPriorityFive() {
        System.out.println("Test priority 5");
    }

    @Test(4)
    @CsvSource("10,Test,40,false")
    void testPriorityFour(int a, String b, int c, boolean d){
        System.out.println("Test priority 4 with parsed: ");
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        System.out.println("d = " + d);
    }

}

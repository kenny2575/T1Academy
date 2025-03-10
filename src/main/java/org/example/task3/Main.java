package org.example.task3;

public class Main {
    public static void main(String[] args) {
        var customThreadPool = new CustomThreadPool(5);

        for (int i = 0; i < 10; i++) {
            customThreadPool.execute(Main.meth(i));
        }
        customThreadPool.shutdown();
        customThreadPool.awaitTermination();
        System.out.println("All tasks completed.");
        customThreadPool.execute(Main.meth(15));

    }

    private static void waitASecond(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
        }
    }

    private static Runnable meth(int taskId) {
        return () -> {
            System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName());
            Main.waitASecond(taskId);
        };
    }
}

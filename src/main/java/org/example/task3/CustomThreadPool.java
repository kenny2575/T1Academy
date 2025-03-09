package org.example.task3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThreadPool {

    private final List<Worker> threads;
    private final LinkedList<Runnable> tasks;
    private final Lock lock = new ReentrantLock();
    private final Condition taskAvailable = lock.newCondition();
    private volatile boolean isShutdown = false;

    public CustomThreadPool(int nThreads) {
        tasks = new LinkedList<>();
        threads = new ArrayList<>(nThreads);
        for (int i = 0; i < nThreads; i++) {
            var thread = new Worker();
            threads.add(thread);
            thread.start();
        }
    }

    public void execute(Runnable task) {
        lock.lock();
        try {
            if (this.isShutdown) {
                throw new IllegalStateException("Thread pool is shutdown");
            }
            tasks.add(task);
            taskAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        lock.lock();

        try {
            isShutdown = true;
            taskAvailable.signalAll();
        } finally {
            lock.unlock();
        }

        for (Worker worker : threads) {
            worker.interrupt();
        }
    }

    public void awaitTermination() {
        for (Worker worker : threads) {
            try {
                worker.join();
            } catch (InterruptedException ignored) {}
        }
    }

    private class Worker extends Thread {
        @Override
        public void run() {
            System.out.println("Запуск потока");
            while (true) {
                Runnable task;
                lock.lock();
                try {
                    while (tasks.isEmpty()) {
                        if (isShutdown) {
                            System.out.println("Выход из потока Shutdown");
                            return;
                        }
                        try {
                            taskAvailable.await();
                        } catch (InterruptedException e) {
                            System.out.println("Выход из потока Interrupted");
                            return;
                        }
                    }
                    task = tasks.poll();
                } finally {
                    lock.unlock();
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

    }

}

package com.ericliu.common.util.shutdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShutDownHelper {
    static ExecutorService pool = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {

        pool.submit(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("==========>" + i);
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                shutDownGracefully(pool, 60);
            }
        }, "ShutdownHook"));

        System.out.println("finish");
    }

    private static void shutDownGracefully(ExecutorService threadPool, long awitSec) {
        //新任务禁止进入进入
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(awitSec, TimeUnit.SECONDS)) {
                threadPool.shutdown();

                if (!threadPool.awaitTermination(awitSec, TimeUnit.SECONDS)) {
                    //停止失败,打印Log
                }
            }
        } catch (InterruptedException e) {
            threadPool.shutdown();
            Thread.currentThread().interrupt();
        }
    }
}

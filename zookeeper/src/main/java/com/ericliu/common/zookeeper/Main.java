package com.ericliu.common.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: liuhaoeric
 * Create time: 2019/11/07
 * Description:
 */
public class Main {
    public static void main(String[] args) {
        ExecutorService t_pool = Executors.newFixedThreadPool(10);
        int z=0;
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            t_pool.execute(() -> {
                while (true) {
                    ZkLeaderSelector zkLeaderSelector = new ZkLeaderSelector("127.0.0.1:2181", "/zklock", finalI + "",
                            new LeaderSelectorListenerAdapter() {
                                @Override
                                public void takeLeadership(CuratorFramework client) throws Exception {
                                    System.out.println("我抢到锁了,我是Leader 然后e.printStackTrace();睡5秒"+finalI);
                                    //处理逻辑
                                    try {
                                        Thread.sleep(5000L);
                                    } catch (InterruptedException e) {

                                    }
                                }
                            });

                    zkLeaderSelector.start();
                }
            });
        }
    }
}

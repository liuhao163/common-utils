package com.ericliu.common.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: liuhaoeric
 * Create time: 2019/11/07
 * Description:利用Curator框架实现的分布式锁
 */
public class ZkLeaderSelector {

    private static final Logger logger = LoggerFactory.getLogger(ZkLeaderSelector.class);

    private  LeaderSelector leaderSelector;
    private  CuratorFramework zkClient;
    private String zkServers;
    private String path;
    private String leaderSelectorId;
    protected  LeaderSelectorListenerAdapter leaderListener;

    /**
     *
     * @param zkServers zkServer
     * @param path zk地址
     * @param leaderSelectorId ZkLeaderSelector指定的id
     * @param leaderListener 监听器类
     */
    public ZkLeaderSelector(String zkServers, String path, String leaderSelectorId, LeaderSelectorListenerAdapter leaderListener) {
        this.zkServers = zkServers;
        this.path = path;
        this.leaderSelectorId = leaderSelectorId;
        this.leaderListener=leaderListener;
    }

    public void start(){
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkServers)
                .sessionTimeoutMs(3000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zkClient.start();
        try {
            zkClient.blockUntilConnected();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        leaderSelector = new LeaderSelector(zkClient, path, leaderListener);
        leaderSelector.setId(leaderSelectorId);
        leaderSelector.autoRequeue();
        leaderSelector.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                close();
            }
        });
    }

    public String getZkServers() {
        return zkServers;
    }

    public String getPath() {
        return path;
    }

    public String getLeaderSelectorId() {
        return leaderSelectorId;
    }

    public void relinquish() {
        // Relinquish from Active state
        if (leaderSelector.hasLeadership()) {
            logger.warn("Pod {} attempt to relinquish from Active state.", leaderSelectorId);
            try {
                leaderSelector.interruptLeadership();
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (leaderSelector.hasLeadership()) {
                logger.info("Pod {} is still in Active state.", leaderSelectorId);
            } else {
                logger.info("Pod {} relinquished from Active state.", leaderSelectorId);
            }
        }
    }

    private void close() {
        if (leaderSelector != null) {
            if (leaderSelector.hasLeadership()) {
                logger.info("Pod {} shutdown and relinquished from Active state.", leaderSelectorId);
            } else {
                logger.info("Pod {} shutdown, was in Standby state.", leaderSelectorId);
            }
            zkClient.close();
            leaderSelector.close();
        }
    }
}

package org.spiderflow.context;

import org.spiderflow.concurrent.SpiderFlowThreadPoolExecutor.SubThreadPoolExecutor;
import org.spiderflow.model.SpiderNode;
import org.spiderflow.model.SpiderOutput;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import static org.spiderflow.utils.Constants.*;

/**
 * 爬虫上下文
 *
 * @author jmxd
 */
public class SpiderContext extends HashMap<String, Object> {

    private String id = UUID.randomUUID().toString().replace("-", "");

    /**
     * 流程ID
     */
    private String flowId;

    private static final long serialVersionUID = 8379177178417619790L;

    /**
     * 流程执行线程
     */
    private SubThreadPoolExecutor threadPool;

    /**
     * 根节点
     */
    private SpiderNode rootNode;

    /**
     * 爬虫是否运行中
     */
    private volatile boolean running = true;

    /**
     * Future队列
     */
    private LinkedBlockingQueue<Future<?>> futureQueue = new LinkedBlockingQueue<>();

    /**
     * Cookie上下文
     */
    private CookieContext cookieContext = new CookieContext();

    public List<SpiderOutput> getOutputs() {
        return Collections.emptyList();
    }

    public <T> T get(String key) {
        return (T) super.get(key);
    }

    public <T> T get(String key, T defaultValue) {
        T value = this.get(key);
        return value == null ? defaultValue : value;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public LinkedBlockingQueue<Future<?>> getFutureQueue() {
        return futureQueue;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void addOutput(SpiderOutput output) {

    }

    public SubThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(SubThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    public SpiderNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(SpiderNode rootNode) {
        this.rootNode = rootNode;
        this.fillGlobalCookieList(rootNode);
    }

    /**
     * 节点的全局 cookie配置
     * @param node
     */
    public void fillGlobalCookieList(SpiderNode node) {
        List<Map<String, String>> listJsonValue = rootNode.getListJsonValue(COOKIE_NAME, COOKIE_VALUE, COOKIE_DOMAIN );
        if (listJsonValue.isEmpty()) {
            return;
        }
        for (Map<String, String> map : listJsonValue) {
            cookieContext.addCookie(map.get(COOKIE_NAME), map.get(COOKIE_VALUE), map.get(COOKIE_DOMAIN));
        }
    }

    public String getId() {
        return id;
    }

    public CookieContext getCookieContext() {
        return cookieContext;
    }

    public void pause(String nodeId, String event, String key, Object value) {
    }

    public void resume() {
    }

    public void stop() {
    }

}

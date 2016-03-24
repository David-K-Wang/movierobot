package com.kingco.movierobot.crawl.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingco.movierobot.crawl.task.BaseSpiderTask;

public class SpiderTaskContext implements Runnable {

    protected static Logger logger = LoggerFactory.getLogger(SpiderTaskContext.class);

    private String contextName = "";

    /**
     * Tasks need to be executed by sequence
     */
    private List<BaseSpiderTask> taskChain = new ArrayList<BaseSpiderTask>();

    /**
     * Context local information
     */
    private Map<String, Object> ctxLocalMap = new HashMap<String, Object>();

    public SpiderTaskContext(String name, BaseSpiderTask... baseSpiderTasks) {
        this.contextName = name;
        for (BaseSpiderTask task : baseSpiderTasks) {
            task.setSpiderTaskContext(this);
        }
        this.taskChain.addAll(Arrays.asList(baseSpiderTasks));
    }

    public List<BaseSpiderTask> getTaskChain() {
        return taskChain;
    }

    public void setTaskChain(List<BaseSpiderTask> taskChain) {
        this.taskChain = taskChain;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    private Map<String, Object> getCtxLocalMap() {
        return ctxLocalMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCtxLocalItem(String key) {
        if (getCtxLocalMap().containsKey(key)) {
            return (T) getCtxLocalMap().get(key);
        }
        return null;
    }

    public boolean containsCtxLocalItem(String key) {
        return getCtxLocalMap().containsKey(key);
    }

    public void putCtxLocalItem(String key, Object value) {
        getCtxLocalMap().put(key, value);
    }

    public void clearCtxLocalMap() {
        this.ctxLocalMap = new HashMap<String, Object>();
    }

    /**
     * Execute tasks by sequence
     */
    public void fireTaskChain() {
        if (CollectionUtils.isNotEmpty(this.getTaskChain())) {
            for (BaseSpiderTask task : this.getTaskChain()) {
                task.fire();
            }
        }
    }

    @Override
    public void run() {
        logger.info("Fire spider task chain - {}", this.getContextName());
        this.fireTaskChain();
        logger.info("Finish spider task chain - {}", this.getContextName());
    }

}

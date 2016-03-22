package com.wk.crawler.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

public class SpiderTaskContext implements Runnable {

    private String contextName = "";

    private String spiderSource = "";

    /**
     * Tasks need to be executed by sequence
     */
    private List<BaseSpiderTask> taskChain = new ArrayList<BaseSpiderTask>();

    /**
     * Context local information
     */
    private Map<String, Object> ctxLocal = new HashMap<String, Object>();

    public SpiderTaskContext(String name, String source, BaseSpiderTask... baseSpiderTasks) {
        this.contextName = name;
        this.setSpiderSource(source);
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

    public Map<String, Object> getCtxLocal() {
        return ctxLocal;
    }

    public void setCtxLocal(Map<String, Object> ctxLocal) {
        this.ctxLocal = ctxLocal;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
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
        this.fireTaskChain();
    }

    public String getSpiderSource() {
        return spiderSource;
    }

    public void setSpiderSource(String spiderSource) {
        this.spiderSource = spiderSource;
    }
}

package com.wk.crawler.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public abstract class BaseSpiderTask implements PageProcessor {

    protected static Logger logger = LoggerFactory.getLogger(BaseSpiderTask.class);

    private SpiderTaskContext taskCtx;

    public abstract int getProcessThreadNum();

    public abstract List<String> getTargetRequests(Page page);

    public abstract Pipeline getPipeline();

    public abstract String[] getEntranceRequests();

    public abstract String getSpiderName();

    public SpiderTaskContext getTaskCtx() {
        return this.taskCtx;
    }

    public void setSpiderTaskContext(SpiderTaskContext taskCtx) {
        this.taskCtx = taskCtx;
    }

    public void fire() {
        logger.info("Start spider task - {}", this.getSpiderName());
        Spider.create(this).addUrl(this.getEntranceRequests()).addPipeline(this.getPipeline())
                .thread(this.getProcessThreadNum()).run();
        logger.info("Finish spider task");
    }
}

package com.wk.crawler.processor.impl;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.wk.crawler.pipeline.SpiderTaskContextPipeline;
import com.wk.crawler.processor.BaseSpiderTask;

public class NuomiCinemaSpiderTask extends BaseSpiderTask {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static String taskName = "NuomiCinemaSpiderTask";

    private int processNum = 5;

    private String[] entranceUrls = new String[] { "http://nj.nuomi.com/cinema/" };

    @Override
    public Site getSite() {
        return this.site;
    }

    @Override
    public void process(Page page) {
        page.addTargetRequests(this.getTargetRequests(page));
        page.putField("cinemaId", page.getUrl().regex("http://nj.nuomi.com/cinema/([a-z0-9]{2,30})").toString());
        page.putField(
                "cinemaName",
                page.getHtml()
                        .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-branch']/div[@class='w-cinema-branch clearfix']/div[@class='cinema-info']/h3/text()")
                        .toString());
        page.putField(
                "cinemaAddress",
                page.getHtml()
                        .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-branch']/div[@class='w-cinema-branch clearfix']/div[@class='cinema-info']/p[@class='cb-address']/text()")
                        .toString());
        page.putField(
                "cinemaPhoneNum",
                page.getHtml()
                        .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-branch']/div[@class='w-cinema-branch clearfix']/div[@class='cinema-info']/p[@class='cb-tel']/text()")
                        .toString());
        if (page.getResultItems().get("cinemaName") == null) {
            // skip this page
            page.setSkip(true);
        }
    }

    @Override
    public int getProcessThreadNum() {
        return this.processNum;
    }

    @Override
    public List<String> getTargetRequests(Page page) {
        return page.getHtml().links().regex("(http://nj.nuomi.com/cinema/([a-z0-9]{2,30}))").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new SpiderTaskContextPipeline(this.getTaskCtx());
    }

    @Override
    public String[] getEntranceRequests() {
        return this.entranceUrls;
    }

    @Override
    public String getSpiderName() {
        return taskName;
    }
}

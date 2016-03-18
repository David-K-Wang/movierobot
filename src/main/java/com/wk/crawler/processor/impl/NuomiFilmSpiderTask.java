package com.wk.crawler.processor.impl;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.wk.crawler.constant.CrawlerConstants;
import com.wk.crawler.pipeline.SpiderTaskContextPipeline;
import com.wk.crawler.processor.BaseSpiderTask;

public class NuomiFilmSpiderTask extends BaseSpiderTask {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static String taskName = "NuomiFilmSpiderTask";

    private int processNum = 5;

    private String[] entranceUrls = new String[] { "http://nj.nuomi.com/movie/" };

    @Override
    public Site getSite() {
        return this.site;
    }

    @Override
    public void process(Page page) {
        page.addTargetRequests(this.getTargetRequests(page));
        page.putField("filmId", page.getUrl().regex("http://nj.nuomi.com/film/([0-9]{2,30})").toString());
        page.putField(
                CrawlerConstants.RESULT_KEY_FILM_NAME,
                page.getHtml()
                        .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-main clearfix']/div[@class='p-cinema-left']/div[@class='w-cinema-detail clearfix']/div[@class='content']/h2/a/text()")
                        .toString());
        if (page.getResultItems().get(CrawlerConstants.RESULT_KEY_FILM_NAME) == null) {
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
        return page.getHtml().links().regex("(http://nj.nuomi.com/film/[0-9]{2,30})").all();
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

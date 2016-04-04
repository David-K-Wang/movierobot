package com.kingco.movierobot.crawl.task.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingco.movierobot.crawl.constant.CrawlConstants;
import com.kingco.movierobot.crawl.dto.CinemaDto;
import com.kingco.movierobot.crawl.pipeline.EmptyPipeline;
import com.kingco.movierobot.crawl.process.SpiderTaskContext;
import com.kingco.movierobot.crawl.task.BaseSpiderTask;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

public class NuomiCinemaSpiderTask extends BaseSpiderTask {

    private String taskName = "NuomiCinemaSpiderTask";

    @Override
    public void process(Page page) {
        page.addTargetRequests(this.getTargetRequests(page));

        SpiderTaskContext ctx = this.getTaskCtx();
        Map<String, CinemaDto> cinemaDtoMap;
        if (ctx.containsCtxLocalItem(CrawlConstants.CTX_LOCAL_CINEMA)) {
            cinemaDtoMap = ctx.getCtxLocalItem(CrawlConstants.CTX_LOCAL_CINEMA);
        } else {
            cinemaDtoMap = new HashMap<String, CinemaDto>();
            ctx.putCtxLocalItem(CrawlConstants.CTX_LOCAL_CINEMA, cinemaDtoMap);
        }
        CinemaDto cinemaDto = new CinemaDto();
        cinemaDto.setCinemaId(page.getUrl().regex("http://nj.nuomi.com/cinema/([a-z0-9]{2,30})").toString());
        cinemaDto.setCinemaName(page.getHtml()
                .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-branch']/div[@class='w-cinema-branch clearfix']/div[@class='cinema-info']/h3/text()")
                .toString());
        cinemaDto.setCimemaAddress(page.getHtml()
                .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-branch']/div[@class='w-cinema-branch clearfix']/div[@class='cinema-info']/p[@class='cb-address']/text()")
                .toString());
        cinemaDto.setCimemaPhoneNum(page.getHtml()
                .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-branch']/div[@class='w-cinema-branch clearfix']/div[@class='cinema-info']/p[@class='cb-tel']/text()")
                .toString());

        if (cinemaDto.isValid()) {
            cinemaDtoMap.put(cinemaDto.getCinemaId(), cinemaDto);
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(100);
    }

    @Override
    public int getProcessThreadNum() {
        return 1;
    }

    @Override
    public List<String> getTargetRequests(Page page) {
        return page.getHtml().links().regex("(http://nj.nuomi.com/cinema/([a-z0-9]{2,30}))").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new EmptyPipeline();
    }

    @Override
    public String[] getEntranceRequests() {
        return new String[] { "http://nj.nuomi.com/cinema/" };
    }

    @Override
    public String getSpiderName() {
        return this.taskName;
    }

}

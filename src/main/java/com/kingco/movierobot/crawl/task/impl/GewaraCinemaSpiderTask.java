package com.kingco.movierobot.crawl.task.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.kingco.movierobot.crawl.constant.CrawlConstants;
import com.kingco.movierobot.crawl.dto.CinemaDto;
import com.kingco.movierobot.crawl.pipeline.EmptyPipeline;
import com.kingco.movierobot.crawl.process.SpiderTaskContext;
import com.kingco.movierobot.crawl.task.BaseSpiderTask;

public class GewaraCinemaSpiderTask extends BaseSpiderTask {

    private String taskName = "GewaraCinemaSpiderTask";

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
        cinemaDto.setCinemaId(page.getUrl().regex("http://www.gewara.com/cinema/([0-9]{2,30})").toString());
        cinemaDto
                .setCinemaName(page
                        .getHtml()
                        .xpath("/html/body/div[@class='ui_layout detail_body detail_cinema_warp']/div[@class='detail_head clear']/div[@class='mod_kong']/div[@class='mod_hd detailPa']/div[@class='detailName clear']/h1[@class='left']/text()")
                        .toString());
        cinemaDto
                .setCimemaAddress(page
                        .getHtml()
                        .xpath("/html/body/div[@class='ui_layout detail_body detail_cinema_warp']/div[@class='detail_head clear']/div[@class='mod_kong']/div[@class='mod_bd']/div[@class='ui_left']/div[@class='detail_head_info detailMa']/div[@class='ui_media']/div[@class='ui_text']/div[@class='detail_head_text']/dl[@class='clear'][1]/dd/text()")
                        .toString());
        cinemaDto
                .setCimemaPhoneNum(page
                        .getHtml()
                        .xpath("/html/body/div[@class='ui_layout detail_body detail_cinema_warp']/div[@class='detail_head clear']/div[@class='mod_kong']/div[@class='mod_bd']/div[@class='ui_left']/div[@class='detail_head_info detailMa']/div[@class='ui_media']/div[@class='ui_text']/div[@class='detail_head_text']/dl[@class='clear'][2]/dd/text()")
                        .toString());

        if (cinemaDto.isValid()) {
            cinemaDtoMap.put(cinemaDto.getCinemaId(), cinemaDto);
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(100).addCookie("citycode", "320100");
    }

    @Override
    public int getProcessThreadNum() {
        return 5;
    }

    @Override
    public List<String> getTargetRequests(Page page) {
        return page.getHtml().links().regex("(http://www.gewara.com/cinema/[0-9]{2,30})").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new EmptyPipeline();
    }

    @Override
    public String[] getEntranceRequests() {
        ArrayList<String> urls = new ArrayList<String>();
        urls.add("http://www.gewara.com/nanjing");
        for (int i = 0; i < 10; i++) {
            urls.add("http://www.gewara.com/movie/searchCinema.xhtml?pageNo=" + i);
        }
        return urls.toArray(new String[urls.size()]);
    }

    @Override
    public String getSpiderName() {
        return this.taskName;
    }

}

package com.kingco.movierobot.crawl.task.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingco.movierobot.crawl.constant.CrawlConstants;
import com.kingco.movierobot.crawl.dto.FilmDto;
import com.kingco.movierobot.crawl.pipeline.EmptyPipeline;
import com.kingco.movierobot.crawl.process.SpiderTaskContext;
import com.kingco.movierobot.crawl.task.BaseSpiderTask;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

public class NuomiFilmSpiderTask extends BaseSpiderTask {

    private String taskName = "NuomiFilmSpiderTask";

    @Override
    public void process(Page page) {
        page.addTargetRequests(this.getTargetRequests(page));

        SpiderTaskContext ctx = this.getTaskCtx();
        Map<String, FilmDto> filmDtoMap;
        if (ctx.containsCtxLocalItem(CrawlConstants.CTX_LOCAL_FILM)) {
            filmDtoMap = ctx.getCtxLocalItem(CrawlConstants.CTX_LOCAL_FILM);
        } else {
            filmDtoMap = new HashMap<String, FilmDto>();
            ctx.putCtxLocalItem(CrawlConstants.CTX_LOCAL_FILM, filmDtoMap);
        }
        FilmDto filmDto = new FilmDto();
        filmDto.setFilmId(page.getUrl().regex("http://nj.nuomi.com/film/([0-9]{2,30})").toString());
        filmDto.setFilmName(page.getHtml()
                .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-main clearfix']/div[@class='p-cinema-left']/div[@class='w-cinema-detail clearfix']/div[@class='content']/h2/a/text()")
                .toString());
        filmDto.setFilmDesc(page.getHtml()
                .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-main clearfix']/div[@class='p-cinema-left']/div[@class='w-cinema-detail clearfix']/div[@class='content']/div[@class='de']/p[2]/text()")
                .toString());
        filmDto.setFilmPicUrl(page.getHtml()
                .xpath("/html/body[@class='gl-normal-screen']/div[@class='p-cinema-main clearfix']/div[@class='p-cinema-left']/div[@class='w-cinema-detail clearfix']/img[@class='cinema-img']/@src")
                .toString());

        if (filmDto.isValid()) {
            filmDtoMap.put(filmDto.getFilmId(), filmDto);
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
        return page.getHtml().links().regex("(http://nj.nuomi.com/film/[0-9]{2,30})").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new EmptyPipeline();
    }

    @Override
    public String[] getEntranceRequests() {
        return new String[] { "http://nj.nuomi.com/movie/" };
    }

    @Override
    public String getSpiderName() {
        return this.taskName;
    }

}

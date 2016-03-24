package com.kingco.movierobot.crawl.task.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.kingco.movierobot.crawl.constant.CrawlConstants;
import com.kingco.movierobot.crawl.dto.FilmDto;
import com.kingco.movierobot.crawl.pipeline.EmptyPipeline;
import com.kingco.movierobot.crawl.process.SpiderTaskContext;
import com.kingco.movierobot.crawl.task.BaseSpiderTask;

public class GewaraFilmSpiderTask extends BaseSpiderTask {

    private String taskName = "GewaraFilmSpiderTask";

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
        filmDto.setFilmId(page.getUrl().regex("http://www.gewara.com/movie/([0-9]{2,30})").toString());
        filmDto.setFilmName(page
                .getHtml()
                .xpath("/html/body/div[@class='clear']/div[@class='mod_top_banner']/div[@class='ui_layout']/div[@class='movieInfo']/div[@class='detail_head_name']/div[@class='clear']/h1/text()")
                .toString());
        filmDto.setFilmDesc(page
                .getHtml()
                .xpath("/html/body/div[@class='clear']/div[@class='mod_top_banner']/div[@class='ui_layout']/div[@class='movieInfo']/p[@class='ui_summary_big']/text()")
                .toString());
        filmDto.setFilmPicUrl(page
                .getHtml()
                .xpath("/html/body/div[@class='clear']/div[@class='mod_movie_info']/div[@class='ui_layout clear']/div[@class='detail_head_info']/div[@class='ui_media']/div[@class='ui_pic mr30']/img/@src")
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
        return 5;
    }

    @Override
    public List<String> getTargetRequests(Page page) {
        return page.getHtml().links().regex("(http://www.gewara.com/movie/[0-9]{2,30})").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new EmptyPipeline();
    }

    @Override
    public String[] getEntranceRequests() {
        return new String[] { "http://www.gewara.com/nanjing" };
    }

    @Override
    public String getSpiderName() {
        return this.taskName;
    }

}

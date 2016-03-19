package com.wk.crawler.pipeline;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.wk.crawler.constant.CrawlerConstants;
import com.wk.crawler.dto.CinemaDto;
import com.wk.crawler.dto.FilmDto;
import com.wk.crawler.processor.SpiderTaskContext;

public class SpiderTaskContextPipeline implements Pipeline {
    private SpiderTaskContext ctx;

    public SpiderTaskContextPipeline(SpiderTaskContext ctx) {
        super();
        this.ctx = ctx;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, CinemaDto> cinemaDtoMap;
        if (ctx.getCtxLocal().containsKey(CrawlerConstants.CTX_LOCAL_CINEMA)) {
            cinemaDtoMap = (Map<String, CinemaDto>) ctx.getCtxLocal().get(CrawlerConstants.CTX_LOCAL_CINEMA);
        } else {
            cinemaDtoMap = new HashMap<String, CinemaDto>();
            ctx.getCtxLocal().put(CrawlerConstants.CTX_LOCAL_CINEMA, cinemaDtoMap);
        }

        String cinemaId = resultItems.get(CrawlerConstants.RESULT_KEY_CINEMA_ID);
        String cinemaName = resultItems.get(CrawlerConstants.RESULT_KEY_CINEMA_NAME);
        if (StringUtils.isNotEmpty(cinemaId) && StringUtils.isNotEmpty(cinemaName)) {
            CinemaDto cinemaDto = new CinemaDto();
            cinemaDtoMap.put(cinemaId, cinemaDto);
            cinemaDto.setCinemaId(cinemaId);
            cinemaDto.setCinemaName(cinemaName);
        }

        Map<String, FilmDto> filmDtoMap;
        if (ctx.getCtxLocal().containsKey(CrawlerConstants.CTX_LOCAL_FILM)) {
            filmDtoMap = (Map<String, FilmDto>) ctx.getCtxLocal().get(CrawlerConstants.CTX_LOCAL_FILM);
        } else {
            filmDtoMap = new HashMap<String, FilmDto>();
            ctx.getCtxLocal().put(CrawlerConstants.CTX_LOCAL_FILM, filmDtoMap);
        }

        String filmId = resultItems.get(CrawlerConstants.RESULT_KEY_FILM_ID);
        String filmName = resultItems.get(CrawlerConstants.RESULT_KEY_FILM_NAME);
        String filmPicUrl = resultItems.get("filmPicUrl");
        if (StringUtils.isNotEmpty(filmId) && StringUtils.isNotEmpty(filmName)) {
            FilmDto filmDto = new FilmDto();
            filmDtoMap.put(filmId, filmDto);
            filmDto.setFilmId(cinemaId);
            filmDto.setFilmName(cinemaName);
            filmDto.setFilmPicUrl(filmPicUrl);
        }
    }

    public SpiderTaskContext getCtx() {
        return ctx;
    }

    public void setCtx(SpiderTaskContext ctx) {
        this.ctx = ctx;
    }

}

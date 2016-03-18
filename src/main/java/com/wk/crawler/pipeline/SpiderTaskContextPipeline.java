package com.wk.crawler.pipeline;

import java.util.ArrayList;
import java.util.List;

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
        List<CinemaDto> cinemaFileDtos;
        if (ctx.getCtxLocal().containsKey(CrawlerConstants.CTX_LOCAL_CINEMA)) {
            cinemaFileDtos = (List<CinemaDto>) ctx.getCtxLocal().get(CrawlerConstants.CTX_LOCAL_CINEMA);
        } else {
            cinemaFileDtos = new ArrayList<CinemaDto>();
            ctx.getCtxLocal().put(CrawlerConstants.CTX_LOCAL_CINEMA, cinemaFileDtos);
        }

        String cinemaId = resultItems.get(CrawlerConstants.RESULT_KEY_CINEMA_ID);
        String cinemaName = resultItems.get(CrawlerConstants.RESULT_KEY_CINEMA_NAME);
        if (StringUtils.isNotEmpty(cinemaId) && StringUtils.isNotEmpty(cinemaName)) {
            CinemaDto cinemaDto = new CinemaDto();
            cinemaFileDtos.add(cinemaDto);
            cinemaDto.setCinemaId(cinemaId);
            cinemaDto.setCinemaName(cinemaName);
        }

        List<FilmDto> filmDtos;
        if (ctx.getCtxLocal().containsKey(CrawlerConstants.CTX_LOCAL_FILM)) {
            filmDtos = (List<FilmDto>) ctx.getCtxLocal().get(CrawlerConstants.CTX_LOCAL_FILM);
        } else {
            filmDtos = new ArrayList<FilmDto>();
            ctx.getCtxLocal().put(CrawlerConstants.CTX_LOCAL_FILM, filmDtos);
        }

        String filmId = resultItems.get(CrawlerConstants.RESULT_KEY_FILM_ID);
        String filmName = resultItems.get(CrawlerConstants.RESULT_KEY_FILM_NAME);
        if (StringUtils.isNotEmpty(filmId) && StringUtils.isNotEmpty(filmName)) {
            FilmDto filmDto = new FilmDto();
            filmDtos.add(filmDto);
            filmDto.setFilmId(cinemaId);
            filmDto.setFilmName(cinemaName);
        }
    }

    public SpiderTaskContext getCtx() {
        return ctx;
    }

    public void setCtx(SpiderTaskContext ctx) {
        this.ctx = ctx;
    }

}

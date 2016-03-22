package com.wk.crawler.processor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.wk.crawler.constant.CrawlerConstants;
import com.wk.crawler.dto.CinemaDto;
import com.wk.crawler.dto.FilmDto;
import com.wk.crawler.pipeline.NuomiDbPipeline;
import com.wk.crawler.processor.BaseSpiderTask;

public class NuomiTicketSpiderTask extends BaseSpiderTask {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public static String taskName = "NuomiTicketSpiderTask";

    private int processNum = 5;

    @Override
    public Site getSite() {
        return this.site;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(Page page) {
        page.addTargetRequests(this.getTargetRequests(page));
        String cinemaId = page.getUrl().regex("cinemaid=([a-z0-9]{2,30})").toString();
        String filmId = page.getUrl().regex("mid=([0-9]{2,30})").toString();

        page.putField("cinemaId", cinemaId);
        page.putField("filmId", filmId);

        Map<String, CinemaDto> cinemaMap = (Map<String, CinemaDto>) this.getTaskCtx().getCtxLocal()
                .get(CrawlerConstants.CTX_LOCAL_CINEMA);
        Map<String, FilmDto> filmMap = (Map<String, FilmDto>) this.getTaskCtx().getCtxLocal()
                .get(CrawlerConstants.CTX_LOCAL_FILM);
        CinemaDto cinemaDto = cinemaMap.get(cinemaId);
        FilmDto filmDto = filmMap.get(filmId);

        if (cinemaDto != null) {
            page.putField("cinemaName", cinemaDto.getCinemaName());
            page.putField("cinemaAddress", cinemaDto.getCimemaAddress());
            page.putField("cinemaPhoneNum", cinemaDto.getCimemaPhoneNum());
        }
        if (filmDto != null) {
            page.putField("filmName", filmDto.getFilmName());
            page.putField("filmPicUrl", filmDto.getFilmPicUrl());
            page.putField("filmDesc", filmDto.getFilmDesc());
        }

        page.putField(
                "date1",
                page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/dl[@id='j-movie-date']/dd[@class='hover']/a[@class='movie-date']/text()")
                        .toString());
        page.putField(
                "date2",
                page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/dl[@id='j-movie-date']/dd[2]/a[@class='movie-date']/text()")
                        .toString());

        for (int dateNum = 1; dateNum <= 2; dateNum++) {
            for (int i = 1; i <= 20; i++) {
                String row = page
                        .getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                + i + "]/td[1]/text()").toString();

                if (StringUtils.isEmpty(row)) {
                    continue;
                }

                page.putField(
                        "time_date" + dateNum + "_slot" + i,
                        page.getHtml()
                                .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                        + i + "]/td[1]/text()").toString());
                page.putField(
                        "style_date" + dateNum + "_slot" + i,
                        page.getHtml()
                                .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                        + i + "]/td[2]/text()").toString());

                page.putField(
                        "hall_date" + dateNum + "_slot" + i,
                        page.getHtml()
                                .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                        + i + "]/td[3]/text()").toString());
                page.putField(
                        "discountPrice_date" + dateNum + "_slot" + i,
                        page.getHtml()
                                .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                        + i + "]/td[4]/span[@class='hover nuomi-price']/text()").toString());

                page.putField(
                        "originPrice_date" + dateNum + "_slot" + i,
                        page.getHtml()
                                .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                        + i + "]/td[4]/del/text()").toString());

                page.putField(
                        "purchaseUrl_date" + dateNum + "_slot" + i,
                        page.getHtml()
                                .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                        + i + "]/td[5]/a[@class='btn-choose-seat']/@href").toString());
            }
        }

        if (page.getResultItems().get("cinemaId") == null || page.getResultItems().get("time_date1_slot1") == null) {
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
        return page.getHtml().links().regex("(http://nj.nuomi.com/pcindex/main/timetable*)").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new NuomiDbPipeline(getTaskCtx());
    }

    @SuppressWarnings("unchecked")
    @Override
    public String[] getEntranceRequests() {
        List<String> entranceUrls = new ArrayList<String>();
        Map<String, CinemaDto> cinemaDtoMap = (Map<String, CinemaDto>) this.getTaskCtx().getCtxLocal()
                .get(CrawlerConstants.CTX_LOCAL_CINEMA);
        Map<String, FilmDto> filmDtoMap = (Map<String, FilmDto>) this.getTaskCtx().getCtxLocal()
                .get(CrawlerConstants.CTX_LOCAL_FILM);
        for (String cinemaId : cinemaDtoMap.keySet()) {
            for (String filmId : filmDtoMap.keySet()) {
                entranceUrls.add("http://nj.nuomi.com/pcindex/main/timetable?cinemaid=" + cinemaId + "&mid=" + filmId);
            }
        }

        return entranceUrls.toArray(new String[entranceUrls.size()]);
    }

    @Override
    public String getSpiderName() {
        return taskName;
    }
}

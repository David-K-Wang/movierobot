package com.kingco.movierobot.crawl.task.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.kingco.movierobot.common.bo.MovieTicketInfo;
import com.kingco.movierobot.crawl.constant.CrawlConstants;
import com.kingco.movierobot.crawl.dto.CinemaDto;
import com.kingco.movierobot.crawl.dto.FilmDto;
import com.kingco.movierobot.crawl.pipeline.DatabaseTicketPipeline;
import com.kingco.movierobot.crawl.process.SpiderTaskContext;
import com.kingco.movierobot.crawl.task.BaseSpiderTask;
import com.wk.crawler.constant.CrawlerConstants;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

public class NuomiTicketSpiderTask extends BaseSpiderTask {

    private String taskName = "NuomiTicketSpiderTask";

    @Override
    public void process(Page page) {
        page.addTargetRequests(this.getTargetRequests(page));

        SpiderTaskContext ctx = this.getTaskCtx();
        Map<String, FilmDto> filmDtoMap = ctx.getCtxLocalItem(CrawlConstants.CTX_LOCAL_FILM);
        Map<String, CinemaDto> cinemaDtoMap = ctx.getCtxLocalItem(CrawlConstants.CTX_LOCAL_CINEMA);

        Map<String, MovieTicketInfo> ticketMap;
        if (ctx.containsCtxLocalItem(CrawlConstants.CTX_LOCAL_TICKET)) {
            ticketMap = ctx.getCtxLocalItem(CrawlConstants.CTX_LOCAL_TICKET);
        } else {
            ticketMap = new ConcurrentHashMap<String, MovieTicketInfo>();
            ctx.putCtxLocalItem(CrawlConstants.CTX_LOCAL_TICKET, ticketMap);
        }

        String filmId = page.getUrl().regex("mid=([0-9]{2,30})").toString();
        String cinemaId = page.getUrl().regex("cinemaid=([a-z0-9]{2,30})").toString();

        FilmDto filmDto = filmDtoMap.get(filmId);
        CinemaDto cinemaDto = cinemaDtoMap.get(cinemaId);
        if (filmDto == null || cinemaDto == null) {
            return;
        }

        MovieTicketInfo ticket = new MovieTicketInfo();

        ticket.setCinemaName(cinemaDto.getCinemaName());
        ticket.setCityName("南京");
        ticket.setMovieName(filmDto.getFilmName());
        ticket.setSourceType("糯米");
        ticket.setMoviePicUrl(filmDto.getFilmPicUrl());
        ticket.setMovieDesc(filmDto.getFilmDesc());

        for (int dateIndex = 1; dateIndex <= 2; dateIndex++) {
            String dateStr = page.getHtml()
                    .xpath("/html/body/div[@id='j-choose-list']/dl[@id='j-movie-date']/dd[@class='hover']/a[@class='movie-date']/text()")
                    .toString();
            if (dateIndex > 1) {
                dateStr = page.getHtml().xpath("/html/body/div[@id='j-choose-list']/dl[@id='j-movie-date']/dd["
                        + dateIndex + "]/a[@class='movie-date']/text()").toString();
            }

            if (StringUtils.isEmpty(dateStr)) {
                continue;
            }

            for (int i = 1; i < 30; i++) {

                String timeSlotStr = page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                + i + "]/td[1]/text()")
                        .toString();
                String style = page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                + i + "]/td[2]/text()")
                        .toString();
                String hall = page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                + i + "]/td[3]/text()")
                        .toString();
                String price = page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                + i + "]/td[4]/span[@class='hover nuomi-price']/text()")
                        .toString();
                String originalPrice = page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                + i + "]/td[4]/del/text()")
                        .toString();
                String sourceUrl = page.getHtml()
                        .xpath("/html/body/div[@id='j-choose-list']/div[@id='j-movie-list0']/div[@class='table']/table/tbody/tr["
                                + i + "]/td[5]/a[@class='btn-choose-seat']/@href")
                        .toString();

                if (i > 0 && StringUtils.isEmpty(timeSlotStr)) {
                    break;
                }

                try {
                    ticket.setMovieTime(new SimpleDateFormat("yyyy.MM.dd HH:mm")
                            .parse(parseDateStr(dateStr) + " " + timeSlotStr.trim()));
                    ticket.setHall(hall);
                    ticket.setCurrentPrice(trimAndParseInteger(price));
                    ticket.setOriginalPrice(trimAndParseInteger(originalPrice));
                    ticket.setSourceUrl(sourceUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                ticketMap.put(ticket.generateId(), ticket);
            }
        }

    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(100).setCharset("UTF-8");
    }

    @Override
    public int getProcessThreadNum() {
        return 3;
    }

    @Override
    public List<String> getTargetRequests(Page page) {
        return page.getHtml().links().regex("(http://www.gewara.com/movie/ajax/getOpiItem.xhtml*)").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new DatabaseTicketPipeline(this.getTaskCtx());
    }

    @Override
    public String[] getEntranceRequests() {
        List<String> entranceUrls = new ArrayList<String>();
        Map<String, CinemaDto> cinemaDtoMap = this.getTaskCtx().getCtxLocalItem(CrawlerConstants.CTX_LOCAL_CINEMA);
        Map<String, FilmDto> filmDtoMap = this.getTaskCtx().getCtxLocalItem(CrawlerConstants.CTX_LOCAL_FILM);
        for (String cinemaId : cinemaDtoMap.keySet()) {
            for (String filmId : filmDtoMap.keySet()) {
                entranceUrls.add("http://nj.nuomi.com/pcindex/main/timetable?cinemaid=" + cinemaId + "&mid=" + filmId);
            }
        }

        return entranceUrls.toArray(new String[entranceUrls.size()]);
    }

    @Override
    public String getSpiderName() {
        return this.taskName;
    }

    private String parseDateStr(String wrappedDateStr) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Pattern pattern = Pattern.compile("\\S*([0-9]{1,2}.[0-9]{1,2})\\S*");
        Matcher dateMatcher = pattern.matcher(wrappedDateStr);
        return dateMatcher.find() ? (year + "." + dateMatcher.group(1)) : null;
    }

    private int trimAndParseInteger(String intStr) throws Exception {
        Pattern p = Pattern.compile("(\\d+)");
        Matcher matcher = p.matcher(intStr);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new Exception();
    }
}

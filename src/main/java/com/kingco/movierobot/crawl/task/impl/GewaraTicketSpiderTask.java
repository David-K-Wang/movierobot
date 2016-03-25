package com.kingco.movierobot.crawl.task.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.kingco.movierobot.common.bo.MovieTicketInfo;
import com.kingco.movierobot.crawl.constant.CrawlConstants;
import com.kingco.movierobot.crawl.dto.CinemaDto;
import com.kingco.movierobot.crawl.dto.FilmDto;
import com.kingco.movierobot.crawl.pipeline.DatabaseTicketPipeline;
import com.kingco.movierobot.crawl.process.SpiderTaskContext;
import com.kingco.movierobot.crawl.task.BaseSpiderTask;

public class GewaraTicketSpiderTask extends BaseSpiderTask {

    private String taskName = "GewaraTicketSpiderTask";

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
            ticketMap = new HashMap<String, MovieTicketInfo>();
            ctx.putCtxLocalItem(CrawlConstants.CTX_LOCAL_TICKET, ticketMap);
        }

        String filmId = page.getUrl().regex("movieid=([0-9]{2,30})").toString();
        String cinemaId = page.getUrl().regex("cid=([0-9]{2,30})").toString();
        String dateStr = page.getUrl().regex("fyrq=([0-9]{2,4}-[0-9]{2,4}-[0-9]{2,4})").toString();

        FilmDto filmDto = filmDtoMap.get(filmId);
        CinemaDto cinemaDto = cinemaDtoMap.get(cinemaId);
        if (filmDto == null || cinemaDto == null) {
            return;
        }

        MovieTicketInfo ticket = new MovieTicketInfo();

        ticket.setCinemaName(cinemaDto.getCinemaName());
        ticket.setCityName("南京");
        ticket.setMovieName(filmDto.getFilmName());
        ticket.setSourceType("格瓦拉");

        for (int i = 1; i < 30; i++) {
            String liIndex = "[" + i + "]";

            String timeSlotStr = page
                    .getHtml()
                    .xpath("/html/body/div[@class='chooseOpi opend']/div[@class='chooseOpi_body']/ul[@class='clear']/li"
                            + liIndex + "/span[@class='opitime']/b/text()").toString();
            String style = page
                    .getHtml()
                    .xpath("/html/body/div[@class='chooseOpi opend']/div[@class='chooseOpi_body']/ul[@class='clear']/li"
                            + liIndex + "/span[@class='opiEdition']/em[@class='left']/text()").toString();

            String hall = page
                    .getHtml()
                    .xpath("/html/body/div[@class='chooseOpi opend']/div[@class='chooseOpi_body']/ul[@class='clear']/li"
                            + liIndex + "/span[@class='opiRoom ui_roomType']/label/text()").toString();
            String price = page
                    .getHtml()
                    .xpath("/html/body/div[@class='chooseOpi opend']/div[@class='chooseOpi_body']/ul[@class='clear']/li"
                            + liIndex + "/span[@class='opiPrice']/b/text()").toString();
            String originalPrice = page
                    .getHtml()
                    .xpath("/html/body/div[@class='chooseOpi opend']/div[@class='chooseOpi_body']/ul[@class='clear']/li"
                            + liIndex + "/span[@class='opiPrice']/em/text()").toString();
            String sourceUrl = page
                    .getHtml()
                    .xpath("/html/body/div[@class='chooseOpi opend']/div[@class='chooseOpi_body']/ul[@class='clear']/li"
                            + liIndex + "/span[@class='opiurl']/a/@href").toString();

            if (i > 0 && StringUtils.isEmpty(timeSlotStr)) {
                break;
            }

            try {
                ticket.setMovieTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr + " " + timeSlotStr));
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
        return page.getHtml().links().regex("(http://www.gewara.com/movie/ajax/getOpiItem.xhtml*)").all();
    }

    @Override
    public Pipeline getPipeline() {
        return new DatabaseTicketPipeline(this.getTaskCtx());
    }

    @Override
    public String[] getEntranceRequests() {
        List<String> entranceUrls = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, CinemaDto> cinemaDtoMap = this.getTaskCtx().getCtxLocalItem(CrawlConstants.CTX_LOCAL_CINEMA);
        Map<String, FilmDto> filmDtoMap = this.getTaskCtx().getCtxLocalItem(CrawlConstants.CTX_LOCAL_FILM);
        for (String cinemaId : cinemaDtoMap.keySet()) {
            for (String filmId : filmDtoMap.keySet()) {
                Date date = new Date();
                Date endDate = calculateDaysLater(date, 1);
                while (date.before(endDate)) {
                    entranceUrls.add("http://www.gewara.com/movie/ajax/getOpiItem.xhtml?movieid=" + filmId + "&fyrq="
                            + sdf.format(date) + "&cid=" + cinemaId);
                    date = calculateDaysLater(date, 1);
                }
            }
        }

        return entranceUrls.toArray(new String[entranceUrls.size()]);
    }

    @Override
    public String getSpiderName() {
        return this.taskName;
    }

    private Date calculateDaysLater(Date date, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        Date endDate = calendar.getTime();
        return endDate;
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

package com.wk.crawler.pipeline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.wk.crawler.dao.SpiderTicketInfoDao;
import com.wk.crawler.processor.SpiderTaskContext;
import com.wk.movierobot.model.MovieTicketInfo;
import com.wk.utils.ClassBeanFactory;

public class NuomiDbPipeline implements Pipeline {

    private SpiderTicketInfoDao spiderTicketInfoDao = (SpiderTicketInfoDao) ClassBeanFactory
            .getBean("spiderTicketInfoDao");

    private SpiderTaskContext ctx;

    public NuomiDbPipeline(SpiderTaskContext ctx) {
        super();
        this.ctx = ctx;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        MovieTicketInfo info = new MovieTicketInfo();
        info.setSourceType(ctx.getSpiderSource());
        String cinemaName = resultItems.get("cinemaName");
        info.setCinemaName(cinemaName);
        info.setCityName("南京");
        String filmName = resultItems.get("filmName");
        info.setMovieName(filmName);
        info.setMoviePicUrl((String) resultItems.get("filmPicUrl"));
        info.setMovieDesc((String) resultItems.get("filmDesc"));

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Pattern pattern = Pattern.compile("\\S*([0-9]{1,2}.[0-9]{1,2})\\S*");
        Date[] dateMap = new Date[2];
        String[] dateStrMap = new String[2];

        String date1 = resultItems.get("date1");
        Matcher date1Matcher = pattern.matcher(date1);
        String formattedDate1 = date1Matcher.find() ? (year + "." + date1Matcher.group(1)) : null;
        try {
            dateMap[0] = sdf.parse(formattedDate1);
            dateStrMap[0] = formattedDate1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String date2 = resultItems.get("date2");
        if (StringUtils.isNotEmpty(date2)) {
            Matcher date2Matcher = pattern.matcher(date2);
            String formattedDate2 = date2Matcher.find() ? (year + "." + date2Matcher.group(1)) : null;
            try {
                dateMap[1] = sdf.parse(formattedDate2);
                dateStrMap[1] = formattedDate2;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (int dateNum = 1; dateNum <= 2; dateNum++) {
            for (int i = 1; i <= 20; i++) {
                // Movie Time
                Date movieDate = dateMap[dateNum - 1];
                if (movieDate == null) {
                    continue;
                }

                String timeSlot = resultItems.get("time_date" + dateNum + "_slot" + i);
                if (StringUtils.isEmpty(timeSlot)) {
                    continue;
                }

                Pattern slotPattern = Pattern.compile("([0-9]{1,2}:[0-9]{1,2})");
                Matcher slotMatcher = slotPattern.matcher(timeSlot);
                String startTime = slotMatcher.find() ? slotMatcher.group(1) : "00:00";
                SimpleDateFormat slotSdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                try {
                    Date slotDate = slotSdf.parse(dateStrMap[dateNum - 1] + " " + startTime);
                    info.setMovieTime(slotDate);
                } catch (ParseException e) {
                    continue;
                }

                // Ticket Price
                String discountPrice = resultItems.get("discountPrice_date" + dateNum + "_slot" + i);
                Pattern discountPattern = Pattern.compile("¥([0-9]{1,2})");
                Matcher discountMatcher = discountPattern.matcher(discountPrice);
                try {
                    int discountPriceInt = Integer.parseInt(discountMatcher.find() ? discountMatcher.group(1) : "");
                    info.setCurrentPrice(discountPriceInt);
                } catch (Exception e) {
                    continue;
                }
                String originPrice = resultItems.get("originPrice_date" + dateNum + "_slot" + i);
                Pattern originPattern = Pattern.compile("¥([0-9]{1,2})");
                Matcher originMatcher = originPattern.matcher(originPrice);
                try {
                    int originPriceInt = Integer.parseInt(originMatcher.find() ? originMatcher.group(1) : "");
                    info.setOriginalPrice(originPriceInt);
                } catch (Exception e) {
                    continue;
                }

                // hall
                String hall = resultItems.get("hall_date" + dateNum + "_slot" + i);
                info.setHall(hall);

                // source url
                String sourceUrl = resultItems.get("purchaseUrl_date" + dateNum + "_slot" + i);
                info.setSourceUrl(sourceUrl);

                spiderTicketInfoDao.saveMovieTicketInfo(info);
            }
        }

    }
}

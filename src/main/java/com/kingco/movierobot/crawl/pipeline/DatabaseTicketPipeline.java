package com.kingco.movierobot.crawl.pipeline;

import java.util.Map;
import java.util.Map.Entry;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.kingco.movierobot.common.bo.MovieTicketInfo;
import com.kingco.movierobot.common.dao.SpiderTicketInfoDao;
import com.kingco.movierobot.common.util.ClassBeanFactory;
import com.kingco.movierobot.crawl.constant.CrawlConstants;
import com.kingco.movierobot.crawl.process.SpiderTaskContext;

public class DatabaseTicketPipeline implements Pipeline {

    private SpiderTicketInfoDao spiderTicketInfoDao = (SpiderTicketInfoDao) ClassBeanFactory
            .getBean("spiderTicketInfoDao");

    private SpiderTaskContext ctx;

    public DatabaseTicketPipeline(SpiderTaskContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, MovieTicketInfo> ticketMap;
        if (ctx.containsCtxLocalItem(CrawlConstants.CTX_LOCAL_TICKET)) {
            ticketMap = ctx.getCtxLocalItem(CrawlConstants.CTX_LOCAL_TICKET);
        } else {
            return;
        }

        for (Entry<String, MovieTicketInfo> entry : ticketMap.entrySet()) {
            spiderTicketInfoDao.saveMovieTicketInfo(entry.getValue());
        }
    }
}

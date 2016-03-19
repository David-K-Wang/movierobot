package com.wk.crawler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wk.crawler.constant.CrawlerConstants;
import com.wk.crawler.processor.SpiderFactory;
import com.wk.crawler.processor.SpiderTaskContext;
import com.wk.crawler.service.SpiderService;

@Service
public class SpiderServiceImpl implements SpiderService {

    private static Logger logger = LoggerFactory.getLogger(SpiderServiceImpl.class);

    @Override
    public void fireAllSpiderTask() {
        logger.info("Start firing all the spider tasks");

        logger.info("Starting firing Nuomi spiders");
        SpiderTaskContext nuomiCtx = SpiderFactory.findTaskCtxByName(CrawlerConstants.SPIDER_CTX_NAME_NUOMI);
        new Thread(nuomiCtx).start();

        logger.info("All the spider tasks finished firing");
    }
}

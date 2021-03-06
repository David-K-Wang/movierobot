package com.kingco.movierobot.crawl.service.impl;

import org.springframework.stereotype.Service;

import com.kingco.movierobot.crawl.process.SpiderTaskFactory;
import com.kingco.movierobot.crawl.service.CrawlService;

@Service
public class CrawlServiceImpl implements CrawlService {

    @Override
    public void startCrawl() {
        new Thread(SpiderTaskFactory.findTaskCtxByName("gewara")).start();
        new Thread(SpiderTaskFactory.findTaskCtxByName("nuomi")).start();
    }

}

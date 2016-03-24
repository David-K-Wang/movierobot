package com.kingco.movierobot.crawl;

import com.kingco.movierobot.crawl.process.SpiderTaskFactory;

public class CrawlManager {
    public static void main(String[] args) {
        SpiderTaskFactory.findTaskCtxByName("gewara").fireTaskChain();
    }
}

package com.kingco.movierobot.crawl.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kingco.movierobot.crawl.service.CrawlService;

@Controller
public class CrawlController {

    @Autowired
    private CrawlService crawlService;

    @RequestMapping(value = "crawl")
    public void startCrawl() {
        crawlService.startCrawl();
    }
}

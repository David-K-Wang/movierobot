package com.wk.crawler.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wk.crawler.service.SpiderService;

@Controller
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @RequestMapping(value = "/startCrawl")
    public void startCrawl(HttpServletRequest request, HttpServletResponse response) {
        this.spiderService.fireAllSpiderTask();
    }
}

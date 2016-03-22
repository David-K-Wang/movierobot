package com.wk.crawler.processor;

import java.util.HashMap;
import java.util.Map;

import com.wk.crawler.constant.CrawlerConstants;
import com.wk.crawler.processor.impl.NuomiCinemaSpiderTask;
import com.wk.crawler.processor.impl.NuomiFilmSpiderTask;
import com.wk.crawler.processor.impl.NuomiTicketSpiderTask;

public class SpiderFactory {

    private static Map<String, SpiderTaskContext> spiderTaskRegistry = new HashMap<String, SpiderTaskContext>();

    static {
        registerTaskContext(CrawlerConstants.SPIDER_CTX_NAME_NUOMI, "糯米", (BaseSpiderTask) new NuomiCinemaSpiderTask(),
                (BaseSpiderTask) new NuomiFilmSpiderTask(), (BaseSpiderTask) new NuomiTicketSpiderTask());
    }

    public static SpiderTaskContext findTaskCtxByName(String ctxName) {
        if (spiderTaskRegistry.containsKey(ctxName)) {
            return spiderTaskRegistry.get(ctxName);
        }
        return null;
    }

    public static void registerTaskContext(SpiderTaskContext ctx) {
        spiderTaskRegistry.put(ctx.getContextName(), ctx);
    }

    public static void registerTaskContext(String ctxName, String source, BaseSpiderTask... tasks) {
        spiderTaskRegistry.put(ctxName, new SpiderTaskContext(ctxName, source, tasks));
    }
}

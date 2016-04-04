package com.kingco.movierobot.crawl.process;

import java.util.HashMap;
import java.util.Map;

import com.kingco.movierobot.crawl.task.BaseSpiderTask;
import com.kingco.movierobot.crawl.task.impl.GewaraCinemaSpiderTask;
import com.kingco.movierobot.crawl.task.impl.GewaraFilmSpiderTask;
import com.kingco.movierobot.crawl.task.impl.GewaraTicketSpiderTask;
import com.kingco.movierobot.crawl.task.impl.NuomiCinemaSpiderTask;
import com.kingco.movierobot.crawl.task.impl.NuomiFilmSpiderTask;
import com.kingco.movierobot.crawl.task.impl.NuomiTicketSpiderTask;

public class SpiderTaskFactory {

    private static Map<String, SpiderTaskContext> spiderTaskRegistry = new HashMap<String, SpiderTaskContext>();

    static {
        registerTaskContext("gewara", new GewaraCinemaSpiderTask(), new GewaraFilmSpiderTask(),
                new GewaraTicketSpiderTask());
        registerTaskContext("nuomi", new NuomiCinemaSpiderTask(), new NuomiFilmSpiderTask(),
                new NuomiTicketSpiderTask());
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

    public static void registerTaskContext(String ctxName, BaseSpiderTask... tasks) {
        spiderTaskRegistry.put(ctxName, new SpiderTaskContext(ctxName, tasks));
    }
}

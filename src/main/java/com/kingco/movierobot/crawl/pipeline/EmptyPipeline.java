package com.kingco.movierobot.crawl.pipeline;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class EmptyPipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems, Task task) {
        // Do nothing
    }

}

package com.wn.utils;

import com.wn.model.ArticleChange;
import com.wn.model.ArticleCount;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModifArticleWindowFunction implements WindowFunction<ArticleChange, ArticleCount, String, TimeWindow> {
    private static final Logger LOG = LoggerFactory.getLogger(ModifArticleWindowFunction.class);
    @Override
    public void apply(String key, TimeWindow timeWindow, Iterable<ArticleChange> iterable, Collector<ArticleCount> collector) throws Exception {
        Long count = 0L;
        for (ArticleChange change : iterable) {
            count++;
            LOG.debug("Processing article: {} at window: [{}, {}]",
                    change.getTitle(),
                    timeWindow.getStart(),
                    timeWindow.getEnd());
        }
        LOG.info("Window completed for key: {}, count: {}, window: [{}, {}]",
                key, count,
                timeWindow.getStart(),
                timeWindow.getEnd());

        collector.collect(new ArticleCount(key, count));
    }
}

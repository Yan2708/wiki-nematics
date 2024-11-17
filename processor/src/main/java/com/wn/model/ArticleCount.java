package com.wn.model;

public class ArticleCount {
    private final String title;
    private final Long Count;

    public ArticleCount(String title, Long count) {
        this.title = title;
        Count = count;
    }

    public String getTitle() {
        return title;
    }

    public Long getCount() {
        return Count;
    }

    @Override
    public String toString() {
        return "ArticleCount{" +
                "title='" + title + '\'' +
                ", Count=" + Count +
                '}';
    }
}

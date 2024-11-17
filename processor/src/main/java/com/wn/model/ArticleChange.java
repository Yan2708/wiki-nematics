package com.wn.model;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleChange{
    public Long id;
    public String type;
    public String title;
    public Long namespace;
    public String comment;
    public String parsedcomment;
    public Long timestamp;
    public String user;
    public boolean bot;
    public String server_url;
    public String server_name;
    public String server_script_path;
    public String wiki;
    public boolean minor;
    public boolean patrolled;
    @JsonIgnore
    public ChangeStamp length;
    @JsonIgnore
    public ChangeStamp revision;
    public Long log_id;
    public String log_type;
    public String log_action;
    @JsonIgnore
    public LogParams log_params;
    public String log_action_comment;
    @JsonIgnore
    public Meta meta;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ArticleChange{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}


package com.wn.model;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeStamp{
    public Long old;
    @JsonProperty("new")
    public Long new_;
}
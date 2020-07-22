package com.dont.mapamental.models;

public abstract class Robot {

    protected final Content content;

    public Robot(Content content) {
        this.content = content;
    }

    public abstract void start() throws Exception;


}

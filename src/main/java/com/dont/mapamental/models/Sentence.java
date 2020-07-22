package com.dont.mapamental.models;

import java.util.List;

public class Sentence {
    private String text;
    private List<String> keywords, images;
    private String googleQuery;

    public Sentence(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "text='" + text + '\'' +
                ", keywords=" + keywords +
                ", images=" + images +
                '}';
    }

    public void setGoogleQuery(String googleQuery) {
        this.googleQuery = googleQuery;
    }

    public String getGoogleQuery() {
        return googleQuery;
    }
}

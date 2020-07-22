package com.dont.mapamental.models;

import java.util.ArrayList;
import java.util.List;

public class Content {

    private final int maxSentences = 14;
    private String searchTerm, prefix;
    private String originalContent, sanitizedContent;
    private List<Sentence> sentences;
    private List<String> alreayDownloadedLinks;


    public Content() {
        this.alreayDownloadedLinks = new ArrayList<>();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getSanitizedContent() {
        return sanitizedContent;
    }

    public void setSanitizedContent(String sanitizedContent) {
        this.sanitizedContent = sanitizedContent;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public int getMaxSentences() {
        return maxSentences;
    }

    public List<String> getAlreayDownloadedLinks() {
        return alreayDownloadedLinks;
    }

    @Override
    public String toString() {
        return "Content{" +
                "searchTerm='" + searchTerm + '\'' +
                ", prefix='" + prefix + '\'' +
                ", sanitizedContent='" + sanitizedContent + '\'' +
                ", sentences=" + sentences +
                '}';
    }

}


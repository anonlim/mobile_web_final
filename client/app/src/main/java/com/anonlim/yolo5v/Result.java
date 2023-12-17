package com.anonlim.yolo5v;

import android.util.Log;
import android.util.Pair;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Result {



    @JsonProperty("title")
    private String title;

    @JsonProperty("text")
    private String text;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("published_date")
    private String publishDate;

    @JsonProperty("image")
    private String imageUrl;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public  List<String>  getObjects(List<String> targets){
        if (targets.isEmpty()) {

        }
        StringBuilder patternBuilder = new StringBuilder("\\b\\d+\\s+(");

        for (String target : targets) {
            patternBuilder.append(target).append("|");
        }
        patternBuilder.deleteCharAt(patternBuilder.length() - 1); // Remove last '|'
        patternBuilder.append(")(s)?,?\\s*");

        Pattern regex = Pattern.compile(patternBuilder.toString());
        Matcher matcher = regex.matcher(text);
        String str = matcher.replaceAll("");
        String[] words = str.split("\\s*,\\s*");
        List<String> results = new ArrayList<>();
        for (String w: words
             ) {
            if (!w.isEmpty()){
                results.add(w);
            }
        }
        return results;
    }
}

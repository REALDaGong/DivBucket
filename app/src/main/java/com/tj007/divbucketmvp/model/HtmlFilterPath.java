package com.tj007.divbucketmvp.model;

import org.litepal.crud.LitePalSupport;

public class HtmlFilterPath extends LitePalSupport {
    private URL url;
    //外键

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

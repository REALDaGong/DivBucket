package com.tj007.divbucketmvp.model;

import org.litepal.crud.LitePalSupport;

public class URL extends LitePalSupport {
    private String URL;
    private boolean isActive;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

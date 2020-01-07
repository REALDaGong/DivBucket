package com.tj007.divbucketmvp.model.persistent;

import org.litepal.crud.LitePalSupport;

public class HtmlFilterPath extends LitePalSupport {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String url;

    private String path;

    //mode 可能会用来指定这一条记录是“选择”出来的，还是正则。
    private String mode;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private boolean isActive;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}

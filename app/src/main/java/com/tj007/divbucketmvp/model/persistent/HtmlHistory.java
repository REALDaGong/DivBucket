package com.tj007.divbucketmvp.model.persistent;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class HtmlHistory extends LitePalSupport {
    private String oldData;
    private String curData;
    private Date date;
    private String path;
    private String URL;



    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getCurData() {
        return curData;
    }

    public void setCurData(String curData) {
        this.curData = curData;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

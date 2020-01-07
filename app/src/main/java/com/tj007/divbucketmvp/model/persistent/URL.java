package com.tj007.divbucketmvp.model.persistent;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class URL extends LitePalSupport {
    private int id;
    private String URL;
    private boolean isActive;
    private String note;
    private String cls;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

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
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}

package com.tj007.divbucketmvp.model.warpper;

import java.util.ArrayList;

public class watchingTargetWarpper {
    private String url;
    private String name;
    private String cls;
    private extraSetting setting;
    private ArrayList<String> rule=new ArrayList<>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public ArrayList<String> getRules() {
        return rule;
    }

    public void addRule(String newRule) {
        rule.add(newRule);
    }
}

class extraSetting{

}

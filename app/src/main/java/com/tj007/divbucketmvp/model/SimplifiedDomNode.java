package com.tj007.divbucketmvp.model;

import org.jsoup.nodes.Element;

import java.util.Set;

/** Dom的数据节点，包含节点类型，class，文本，id
 *
 */
public class SimplifiedDomNode {

    private String Type;
    private Set<String> cls;
    private String text;
    private String Id;

    public SimplifiedDomNode(Element element){
        Type=element.nodeName();
        cls=element.classNames();
        text=element.ownText();
        Id=element.id();
    }

    public String getType() {
        return Type;
    }

    public Set<String> getCls() {
        return cls;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return Id;
    }

}

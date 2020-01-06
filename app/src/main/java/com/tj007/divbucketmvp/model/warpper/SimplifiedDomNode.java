package com.tj007.divbucketmvp.model.warpper;

import android.content.Intent;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.Set;

/** Dom的数据节点，包含节点类型，class，文本，id，以及在同级中排第几个
 *  用于在程序中传递，保存的形式跟这个有些不同。
 */
public class SimplifiedDomNode implements Serializable {

    private String Type;
    private Set<String> cls;
    private String text;
    private String Id;
    private int index;

    public SimplifiedDomNode(Element element){
        Type=element.nodeName();
        cls=element.classNames();
        text=element.ownText();
        Id=element.id();
        index=element.siblingIndex();
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

    public int getIndex(){ return index; }

}

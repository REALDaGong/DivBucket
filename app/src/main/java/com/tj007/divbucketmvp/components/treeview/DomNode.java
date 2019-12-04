package com.tj007.divbucketmvp.components.treeview;

import com.tj007.divbucketmvp.model.SimplifiedDomNode;

import org.jsoup.nodes.Element;

public class DomNode extends TreeNode {
    public DomNode(){

        this.value=null;
    }

    public DomNode(Element element){

        this.value=new SimplifiedDomNode(element);
    }

    @Override
    public SimplifiedDomNode getValue() {
        return (SimplifiedDomNode) this.value;
    }

}

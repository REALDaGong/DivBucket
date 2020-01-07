package com.tj007.divbucketmvp.components.treeview;
import android.view.View;

import com.tj007.divbucketmvp.components.treeview.base.BaseNodeViewBinder;
import com.tj007.divbucketmvp.components.treeview.base.BaseNodeViewFactory;



public class MyNodeViewFactory extends BaseNodeViewFactory {

    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {
        return new DomNodeViewBinder(view);
    }

}
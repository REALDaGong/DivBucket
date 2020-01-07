
package com.tj007.divbucketmvp.components.treeview;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.model.warpper.SimplifiedDomNode;
import com.tj007.divbucketmvp.components.treeview.base.CheckableNodeViewBinder;

import java.util.Set;

public class DomNodeViewBinder extends CheckableNodeViewBinder {
    TextView textView;
    ImageView imageView;
    public DomNodeViewBinder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.node_name_view);
        imageView = itemView.findViewById(R.id.arrow_img);
    }

    @Override
    public int getCheckableViewId() {
        return R.id.checkBox;
    }

    @Override
    public void bindView(final TreeNode treeNode) {
        if(((SimplifiedDomNode)treeNode.getValue()).getType().equals("Text")){
            textView.setText(((SimplifiedDomNode)treeNode.getValue()).getText());
        }else {
            SimplifiedDomNode node=(SimplifiedDomNode)treeNode.getValue();
            textView.setText(buildText(node));
        }
        imageView.setRotation(treeNode.isExpanded() ? 90 : 0);
        imageView.setVisibility(treeNode.hasChild() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            imageView.animate().rotation(90).setDuration(200).start();
        } else {
            imageView.animate().rotation(0).setDuration(200).start();
        }
    }

    @Override
    public int getLayoutId() {

        return R.layout.item_first_level;

    }

    private SpannableString buildText(SimplifiedDomNode node){
        StringBuilder sb=new StringBuilder();
        sb.append("<").append(node.getType()).append(">");
        int tagEndAt=sb.length();
        Set<String> cls=node.getCls();
        if(cls.size()!=0){
            sb.append(" class=");
            for(String s:cls){
                sb.append(s);
            }
        }
        int clsEndAt=sb.length();
        String id=node.getId();
        if(!"".equals(id)){
            sb.append(" id=");
            sb.append(id);
        }
        int idEndAt=sb.length();
        SpannableString spannableString=new SpannableString(sb.toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.YELLOW),tagEndAt-1,tagEndAt, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
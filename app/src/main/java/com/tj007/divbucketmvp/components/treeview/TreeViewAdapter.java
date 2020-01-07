/*
 * Copyright 2016 - 2017 ShineM (Xinyuan)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under.
 */

package com.tj007.divbucketmvp.components.treeview;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.tj007.divbucketmvp.components.treeview.base.BaseNodeViewBinder;
import com.tj007.divbucketmvp.components.treeview.base.CheckableNodeViewBinder;
import com.tj007.divbucketmvp.components.treeview.helper.TreeHelper;
import com.tj007.divbucketmvp.components.treeview.base.BaseNodeViewFactory;



public class TreeViewAdapter extends RecyclerView.Adapter {

    private Context context;

    private TreeNode root;

    private List<TreeNode> curNodeList;
    private HashMap<String,List<TreeNode>> treeCache;

    private List<TreeNode> mainNodeList;

    private BaseNodeViewFactory baseNodeViewFactory;

    private View EMPTY_PARAMETER;

    private TreeView treeView;

    TreeViewAdapter(Context context, TreeNode root,
                    @NonNull BaseNodeViewFactory baseNodeViewFactory) {
        this.context = context;
        if(root==null){
            this.root=new TreeNode();
        }else {
            this.root = root;
        }
        this.baseNodeViewFactory = baseNodeViewFactory;

        this.EMPTY_PARAMETER = new View(context);

        this.mainNodeList = new ArrayList<>();
        buildExpandedNodeList();
    }

    public boolean setRoot(TreeNode node){
        //将来改异步？
        clearFilter();
        this.root=node;

        return true;
    }

    private void buildExpandedNodeList() {
        mainNodeList.clear();
        curNodeList=mainNodeList;
        for (TreeNode child : root.getChildren()) {
            insertNode(mainNodeList, child);
        }
    }

    private void insertNode(List<TreeNode> nodeList, TreeNode treeNode) {
        nodeList.add(treeNode);

        if (!treeNode.hasChild()) {
            return;
        }
        if (treeNode.isExpanded()) {
            for (TreeNode child : treeNode.getChildren()) {
                insertNode(nodeList, child);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return curNodeList.get(position).getLevel();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int level) {
        View view = LayoutInflater.from(context).inflate(baseNodeViewFactory
                .getNodeViewBinder(EMPTY_PARAMETER, level).getLayoutId(), parent, false);

        BaseNodeViewBinder nodeViewBinder = baseNodeViewFactory.getNodeViewBinder(view, level);
        nodeViewBinder.setTreeView(treeView);
        return nodeViewBinder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final View nodeView = holder.itemView;
        //搞到数据
        final TreeNode treeNode = curNodeList.get(position);
        final BaseNodeViewBinder viewBinder = (BaseNodeViewBinder) holder;

        if (viewBinder.getToggleTriggerViewId() != 0) {
            View triggerToggleView = nodeView.findViewById(viewBinder.getToggleTriggerViewId());

            if (triggerToggleView != null) {
                triggerToggleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNodeToggled(treeNode);
                        viewBinder.onNodeToggled(treeNode, treeNode.isExpanded());
                    }
                });
            }
        } else if (treeNode.isItemClickEnable()) {
            nodeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNodeToggled(treeNode);
                    viewBinder.onNodeToggled(treeNode, treeNode.isExpanded());
                }
            });
        }

        if (viewBinder instanceof CheckableNodeViewBinder) {
            setupCheckableItem(nodeView, treeNode, (CheckableNodeViewBinder) viewBinder);
        }

        viewBinder.bindView(treeNode);
    }

    private void setupCheckableItem(View nodeView,
                                    final TreeNode treeNode,
                                    final CheckableNodeViewBinder viewBinder) {
        final View view = nodeView.findViewById(viewBinder.getCheckableViewId());

        if (view instanceof Checkable) {
            final Checkable checkableView = (Checkable) view;
            checkableView.setChecked(treeNode.isSelected());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = checkableView.isChecked();
                    selectNode(checked, treeNode);
                    viewBinder.onNodeSelectedChanged(treeNode, checked);
                }
            });
        } else {
            throw new ClassCastException("The getCheckableViewId() " +
                    "must return a CheckBox's id");
        }
    }

    void selectNode(boolean checked, TreeNode treeNode) {
        treeNode.setSelected(checked);

        selectChildren(treeNode, checked);
        selectParentIfNeed(treeNode, checked);
    }

    private void selectChildren(TreeNode treeNode, boolean checked) {
        List<TreeNode> impactedChildren = TreeHelper.selectNodeAndChild(treeNode, checked);
        int index = curNodeList.indexOf(treeNode);
        if (index != -1 && impactedChildren.size() > 0) {
            notifyItemRangeChanged(index, impactedChildren.size() + 1);
        }
    }

    private void selectParentIfNeed(TreeNode treeNode, boolean checked) {
        List<TreeNode> impactedParents = TreeHelper.selectParentIfNeedWhenNodeSelected(treeNode, checked);
        if (impactedParents.size() > 0) {
            for (TreeNode parent : impactedParents) {
                int position = curNodeList.indexOf(parent);
                if (position != -1) notifyItemChanged(position);
            }
        }
    }

    private void onNodeToggled(TreeNode treeNode) {
        treeNode.setExpanded(!treeNode.isExpanded());

        if (treeNode.isExpanded()) {
            expandNode(treeNode);
        } else {
            collapseNode(treeNode);
        }
    }

    @Override
    public int getItemCount() {
        return curNodeList == null ? 0 : curNodeList.size();
    }

    /**
     * Refresh all,this operation is only used for refreshing list when a large of nodes have
     * changed value or structure because it take much calculation.
     */
    void refreshView() {
        buildExpandedNodeList();

        notifyDataSetChanged();
    }

    // Insert a node list after index.
    private void insertNodesAtIndex(int index, List<TreeNode> additionNodes) {
        if (index < 0 || index > curNodeList.size() - 1 || additionNodes == null) {
            return;
        }
        curNodeList.addAll(index + 1, additionNodes);
        notifyItemRangeInserted(index + 1, additionNodes.size());
    }

    //Remove a node list after index.
    private void removeNodesAtIndex(int index, List<TreeNode> removedNodes) {
        if (index < 0 || index > curNodeList.size() - 1 || removedNodes == null) {
            return;
        }
        curNodeList.removeAll(removedNodes);
        notifyItemRangeRemoved(index + 1, removedNodes.size());
    }

    /**
     * Expand node. This operation will keep the structure of children(not expand children)
     */
    void expandNode(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        List<TreeNode> additionNodes = TreeHelper.expandNode(treeNode, false);
        int index = curNodeList.indexOf(treeNode);

        insertNodesAtIndex(index, additionNodes);
    }


    /**
     * Collapse node. This operation will keep the structure of children(not collapse children)
     */
    void collapseNode(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        List<TreeNode> removedNodes = TreeHelper.collapseNode(treeNode, false);
        int index = curNodeList.indexOf(treeNode);

        removeNodesAtIndex(index, removedNodes);
    }

    /**
     * Delete a node from list.This operation will also delete its children.
     */
    void deleteNode(TreeNode node) {
        if (node == null || node.getParent() == null) {
            return;
        }
        List<TreeNode> allNodes = TreeHelper.getAllNodes(root);
        if (allNodes.indexOf(node) != -1) {
            node.getParent().removeChild(node);
        }

        //remove children form list before delete
        collapseNode(node);

        int index = curNodeList.indexOf(node);
        if (index != -1) {
            curNodeList.remove(node);
        }
        notifyItemRemoved(index);
    }
    public void clearFilter(){
        searchQueue.clear();
        curNodeList=mainNodeList;
        notifyDataSetChanged();
    }
    public void filterbyString(String s){

        searchQueue.clear();
        searchQueue.push(root);
        curNodeList=new ArrayList<TreeNode>();
        TreeNode next=null;
        do{
            next=findNextNodeContainString(s);
            if(next!=null) {
                curNodeList.add(next);
            }
        }while (next!=null);
        notifyDataSetChanged();
    }

    public void filterbyTag(String tag){
        searchQueue.clear();
        searchQueue.push(root);
        curNodeList=new ArrayList<TreeNode>();
        TreeNode next=null;
        do{
            next=findNextNodeisType(tag);
            if(next!=null) {
                curNodeList.add(next);
            }
        }while (next!=null);
        notifyDataSetChanged();
    }

    public void filterbyClass(ArrayList<String> classes){
        searchQueue.clear();
        searchQueue.push(root);
        notifyDataSetChanged();
        return;

    }


    private boolean notChanged=true;
    private int cacheLimit=7;
    private void tryFetchCache(String key){
        if(treeCache.containsKey(key) && notChanged){
            curNodeList=treeCache.get(key);
        }
    }
    private void addNewCache(String key,List<TreeNode> list){
        if(treeCache.size()>=cacheLimit){
            //treeCache.remove("123");
        }
        treeCache.put(key,list);
    }
    //java has no generator,and i'm too trash to write my own.
    LinkedList<TreeNode> searchQueue=new LinkedList<>();
    private TreeNode findNextNodeContainString(String s){
        while(!searchQueue.isEmpty()){
            TreeNode node=searchQueue.removeFirst();
            for (TreeNode child:node.getChildren()) {
                searchQueue.addLast(child);
            }
            if(match(node.getValue(),s)){
                return node;
            }

        }
        //and return next one....
        return null;
    }
    private TreeNode findNextNodeisType(String s){
        while(!searchQueue.isEmpty()){
            TreeNode node=searchQueue.removeFirst();
            for (TreeNode child:node.getChildren()) {
                searchQueue.addLast(child);
            }
            if(match(node.getValue(),s)){
                return node;
            }

        }
        //and return next one....
        return null;
    }
    //temp method.
    private boolean match(Object o,String s){
        if(o==null){
            return false;//root.getValue==null
        }
        return(o.toString().contains(s));
    }
    void setTreeView(TreeView treeView) {
        this.treeView = treeView;
    }

    private class filterTask extends AsyncTask<String,Integer,Boolean>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context,"start searching...",Toast.LENGTH_LONG).show();
        }

        //do search here...
        @Override
        protected Boolean doInBackground(String... strings) {

            return null;
        }

        //update UI here...
        @Override
        protected void onProgressUpdate(Integer... integers){

        }
    }
}

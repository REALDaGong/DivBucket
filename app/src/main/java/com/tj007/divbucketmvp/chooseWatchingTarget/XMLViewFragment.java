package com.tj007.divbucketmvp.chooseWatchingTarget;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;


import java.util.List;

import com.example.myapplication.R;

import com.tj007.divbucketmvp.components.treeview.DomNode;
import com.tj007.divbucketmvp.components.treeview.MyNodeViewFactory;
import com.tj007.divbucketmvp.components.treeview.TreeNode;
import com.tj007.divbucketmvp.components.treeview.TreeView;

import org.jetbrains.annotations.NotNull;

//注意这么个事情，转屏会导致activity销毁再创建，可能会导致潜在的空指针问题，可以禁止，但是我想想想别的“办法”？
public class XMLViewFragment extends Fragment implements ChooseWatchingTargetContract.View {

    private DomNode root=new DomNode();
    private TreeView treeView;
    private ViewGroup viewGroup;
    private SearchView searchBar;

    private ChooseWatchingTargetContract.Presenter mPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceBundle){
        View view = inflater.inflate(R.layout.xml_list_fragment,container,false);
        initView(view);
        return view;
    }

    //将来扔进helper。
    private String showSelectedNodes(){
        StringBuilder stringBuilder = new StringBuilder("you have selected:");
        List<TreeNode> selectedNodes = treeView.getSelectedNodes();
        for (int i=0;i<selectedNodes.size();i++){
            if(i<5){
                stringBuilder.append(selectedNodes.get(i).getValue().toString()+",");

            }else{
                stringBuilder.append("...and more");
            }
        }
        return stringBuilder.toString();
    }


    private void setLightStatusBar(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            getActivity().getWindow().setStatusBarColor(Color.WHITE);
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    private void initView(View view) {
        viewGroup = (RelativeLayout) view.findViewById(R.id.container);
        searchBar=view.findViewById(R.id.searchbar);

        setLightStatusBar(viewGroup);

        treeView=new TreeView(root,this.getContext(),new MyNodeViewFactory());
        View treeview = treeView.getView();
        treeview.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT
        ));
        viewGroup.addView(treeview);


        initSearch();
    }

    private void initSearch(){
        searchBar.setSubmitButtonEnabled(true);
        searchBar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){//这是不是得换个控件
                    Log.d("debug","得到焦点");
                }else{
                    Log.d("debug","失去焦点");
                }
            }
        });
        Log.d("debug","lost");
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//会不会报异常？
                if(treeView!=null) {
                    treeView.filter(s);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(s.length()==0){
                    if(treeView!=null) {
                        treeView.clearFilter();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void loadNewDOMTree(DomNode root) {
        if(treeView!=null){
            if(root==null) {
                treeView.refreshTreeView();
            }
            else {
                treeView.setRoot(root);
                treeView.refreshTreeView();
            }
        }
    }


    @Override
    public void showLoading() {
        Toast.makeText(getContext(),"loading",Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideLoading() {
        Toast.makeText(getContext(),"完了",Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(String Message) {
        Toast.makeText(getContext(),Message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void attachPresenter(@NotNull ChooseWatchingTargetContract.Presenter presenter) {
        mPresenter=presenter;
    }
}

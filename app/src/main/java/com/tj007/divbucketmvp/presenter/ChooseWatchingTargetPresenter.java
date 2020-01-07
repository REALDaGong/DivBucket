package com.tj007.divbucketmvp.presenter;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.GetHTMLTask;

import com.tj007.divbucketmvp.components.treeview.TreeNode;
import com.tj007.divbucketmvp.contract.ChooseWatchingTargetContract;
import com.tj007.divbucketmvp.model.DatabaseManager;
import com.tj007.divbucketmvp.model.NetworkDataFetcher;
import com.tj007.divbucketmvp.model.warpper.SimplifiedDomNode;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;
import java.util.List;


// (+_+)? 我好菜

public class ChooseWatchingTargetPresenter implements ChooseWatchingTargetContract.Presenter {

    GetHTMLTask task;
    //emmm,只有一个task，只不过应该不会有多个...
    //我觉得这样的写法也是个问题，可不可以也用泛型绑上？
    private WeakReference<ChooseWatchingTargetContract.View> mChooseTargetView;

    private String CurrentURL;

    NetworkDataFetcher mNetworkDataFetcher=NetworkDataFetcher.getInstance();
    DatabaseManager mDatabaseManager=DatabaseManager.getInstance();

    private TreeNode Root;
    public ChooseWatchingTargetPresenter(@NonNull ChooseWatchingTargetContract.View chooseTargetView){
        mChooseTargetView=new WeakReference<>(chooseTargetView);
        chooseTargetView.attachPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detachView() {
        if(task==null){return;}
    }

    @Override
    public void asyncRequestHTML(String URL){
        CurrentURL=URL;

        mNetworkDataFetcher.getDataAsync(CurrentURL,new AsyncResponse<Element>(){
            @Override
            public void processFinish(Element output, ASYNC_RES_STATE state) {
                buildTreefromElement(output);
                if (state==ASYNC_RES_STATE.ERROR){
                    mChooseTargetView.get().showError("URL不存在或者网络未连接");
                }else{
                    if (mChooseTargetView.get().isActive()) {
                        mChooseTargetView.get().informDomTreeUpdate();
                    } else {
                        nothing();
                    }
                }
            }
        });
    }

    @Override
    public TreeNode getDomTree() {
        return Root;
    }

    private void nothing(){
        //nothing.
        //其实是个致命错误
    }

    private void buildTreefromElement(Element element){
        Root=new TreeNode();
        treeBuilder(element,Root);
    }
    private void treeBuilder(Element DOMroot, TreeNode treeRoot){

        Elements eles=DOMroot.children();
        //把文本抽出来独立做一个节点
        String DOMText=DOMroot.ownText();
        if(!DOMText.isEmpty()) {
            Element textDummyElement = new Element("Text");
            textDummyElement.appendText(DOMText);
            treeRoot.addChild(new TreeNode(jsoupElementToNodes(textDummyElement)));
        }
        for(Element child:eles) {
            TreeNode childTree=new TreeNode(jsoupElementToNodes(child));
            treeRoot.addChild(childTree);
            treeBuilder(child,childTree);
        }
    }
    private SimplifiedDomNode jsoupElementToNodes(Element ele){
        return new SimplifiedDomNode(ele);
    }


    private boolean urlSaved=false;
    @Override
    public void saveAllPath(final List<List<TreeNode>> paths) {
        mDatabaseManager.addSavePathCache(paths,CurrentURL);
    }
}

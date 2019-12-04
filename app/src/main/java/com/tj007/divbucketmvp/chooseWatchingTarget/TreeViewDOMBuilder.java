package com.tj007.divbucketmvp.chooseWatchingTarget;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.GetHTMLTask;

import com.tj007.divbucketmvp.components.treeview.DomNode;
import com.tj007.divbucketmvp.components.treeview.TreeNode;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


// (+_+)? 我好菜

public class TreeViewDOMBuilder implements ChooseWatchingTargetContract.Presenter {

    GetHTMLTask task;//emmm,只有一个task，只不过应该不会有多个...
    //我觉得这样的写法也是个问题，可不可以也用泛型绑上？
    ChooseWatchingTargetContract.View mChooseTargetView;

    public TreeViewDOMBuilder(@NonNull ChooseWatchingTargetContract.View chooseTargetView){
        mChooseTargetView=chooseTargetView;
        chooseTargetView.attachPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detachView() {
        if(task==null)return;
        if(task.getStatus()!= AsyncTask.Status.FINISHED){
            task.cancel(true);//._.?
        }
    }
    //这个神奇写法，我可能脑子坏了
    /*
    public void GetDOMTreefromNetwork(String URL, final AsyncResponse<DomNode> cb){
        GetHTMLTask task=new GetHTMLTask(new AsyncResponse<Element>(){
            @Override
            public void processFinish(Element output, ASYNC_RES_STATE state) {
                DomNode root=buildTreefromElement(output);
                cb.processFinish(root,state);
            }
        });
        task.execute(URL);
    }
    */
    public void asyncRequestHTML(String URL){
        task=new GetHTMLTask(new AsyncResponse<Element>(){
            @Override
            public void processFinish(Element output, ASYNC_RES_STATE state) {
                DomNode root=buildTreefromElement(output);
                if (state==ASYNC_RES_STATE.ERROR){

                }else{
                    if (mChooseTargetView.isActive()) {
                        mChooseTargetView.loadNewDOMTree(root);
                    } else {
                        nothing();
                    }
                }
            }
        });

        task.execute(URL);
    }

    private void nothing(){
        //nothing.
        //其实是个致命错误
    }

    private DomNode buildTreefromElement(Element element){
        DomNode root=new DomNode();
        treeBuilder(element,root);
        return root;
    }
    private void treeBuilder(Element DOMroot, TreeNode treeRoot){

        Elements eles=DOMroot.children();
        //把文本抽出来独立做一个节点
        String DOMText=DOMroot.ownText();
        if(!DOMText.isEmpty()) {
            Element textDummyElement = new Element("Dummy");
            textDummyElement.appendText(DOMText);
            treeRoot.addChild(new DomNode(textDummyElement));
        }
        for(Element child:eles) {
            DomNode childTree=new DomNode(child);
            treeRoot.addChild(childTree);
            treeBuilder(child,childTree);
        }
    }
}

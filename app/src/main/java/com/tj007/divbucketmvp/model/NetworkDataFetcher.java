package com.tj007.divbucketmvp.model;

import android.util.Log;

import androidx.annotation.VisibleForTesting;

import com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.GetHTMLTask;
import com.tj007.divbucketmvp.model.warpper.FakeDoc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkDataFetcher {

    //设为true，就读取一个本地缓存的doc来演示。
    public static boolean debugMode=true;


    private static NetworkDataFetcher Instance=new NetworkDataFetcher();

    private NetworkDataFetcher(){}

    public static NetworkDataFetcher getInstance() {
        return Instance;
    }

    public String getData(String URL){
        return "Not IMPLEMENT";
    }

    public List<String> getData(String URL, List<String> paths){
        List<String> result=new ArrayList<>();

        final CountDownLatch countDownLatch=new CountDownLatch(1);

        getDataAsync(URL, paths, new AsyncResponse<String>() {
            @Override
            public void processFinish(String output, ASYNC_RES_STATE state) {
                if(state==ASYNC_RES_STATE.ERROR){
                    //NO_DATA
                    result.add(output);
                }else {
                    if(state==ASYNC_RES_STATE.STEP) {
                        result.add(output);
                    }else
                    if (state == ASYNC_RES_STATE.SUCCESS) {
                        countDownLatch.countDown();
                    }
                }
            }

            @Override
            public void processFailed(Throwable throwable, ASYNC_RES_STATE state) {
                super.processFailed(throwable, state);
                //进行到这里代表获取URL的这一步已经超时了。
                result.add(NETWORK_FETCHER_ERROR.NO_NETWORK);
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await(10000, TimeUnit.MILLISECONDS);
        }catch (InterruptedException e){
            return null;
        }
        if(countDownLatch.getCount()==0) {
            if(result.size()==0){
                //没有可用的路径查询结果。
                return result;
            }
            if (result.get(0).equals(NETWORK_FETCHER_ERROR.NO_NETWORK)) {
                //无法获取URL
                return result;
            }
        }else{
            //超时了，但是此时后台任务还在运行。
            //不管了，太复杂
            for(String s:result){
                Log.d("debug", "超时了，此时还有数据:"+s);
            }
            result.clear();
            result.add(0,NETWORK_FETCHER_ERROR.TIMEOUT);
        }
        return result;
    }

    public int getDataAsync(final String URL, AsyncResponse callback){
        //try?
        if(debugMode==true){
            callback.processFinish(Jsoup.parse(FakeDoc.doc),ASYNC_RES_STATE.SUCCESS);
        }else {
            GetHTMLTask task = new GetHTMLTask(callback);
            task.execute(URL);
        }
        return 0;
    }

    /**
     *
     * @param URL
     * @param path
     * @param callback 每找到一条就会调一次回调
     * @return
     */

    public void getDataAsync(String URL,final List<String> path, final AsyncResponse callback){
        final ArrayList<String> result=new ArrayList<>();
        getDataAsync(URL,new AsyncResponse<Element>(){
            @Override
            public void processFinish(Element output, ASYNC_RES_STATE state) {
                FilterData(result,path,output,callback);
            }

            @Override
            public void processFailed(Throwable throwable, ASYNC_RES_STATE state) {
                super.processFailed(throwable, state);
                callback.processFailed(throwable,state);
            }
        });
    }

    /**
     *
     * @param writeTo
     * @param root
     * @param callback
     * 这个方法每处理一条路径(正则算一个)都会调一次回调。
     * TODO 搞成多线程的worker?
     */
    private void FilterData(List<String> writeTo,final List<String> path,final Element root,AsyncResponse<String> callback){
        boolean no=false;
        final String htmlText=root.wholeText();
        for (String p:path
             ) {
            Element ptr=root;
            ParsedPath cpath=PathParser.parse(p);
            //regex
            if(cpath.isRegex()){
                String matchResult="";
                Pattern r=Pattern.compile(cpath.gotoHead().name);
                Matcher m=r.matcher(htmlText);
                while(m.find()){
                    matchResult.concat(m.group());
                    matchResult.concat("\n");
                }
                if(matchResult.isEmpty()){
                    callback.processFinish(NETWORK_FETCHER_ERROR.NO_DATA,ASYNC_RES_STATE.ERROR);
                }else{
                    callback.processFinish(matchResult,ASYNC_RES_STATE.STEP);
                }
                continue;
            }
            PathElement pele=cpath.gotoHead();
            while(pele.index!=-1){
                try {
                    //callback.processFinish(pele.name,ASYNC_RES_STATE.ERROR);

                    Elements eles = ptr.children();
                    ptr = eles.get(pele.index-1);
                    if(!(pele.name.equals(ptr.tagName()))){
                        //name对不上
                        no=true;
                        break;
                    }
                    pele = cpath.getNext();
                    if("Text".equals(pele.name)){
                        break;
                    }
                    if(pele.name=="end"){
                        break;
                    }
                }catch (IndexOutOfBoundsException e){
                    no=true;
                    break;
                }
            }
            if(no){
                callback.processFinish(NETWORK_FETCHER_ERROR.NO_DATA,ASYNC_RES_STATE.ERROR);
                no=false;
                continue;
            }
            String s=ptr.wholeText();
            if(s==null||"".equals(s)){
                s="无文本";
            }
            callback.processFinish(s,ASYNC_RES_STATE.STEP);
            writeTo.add(ptr.ownText());

        }
        callback.processFinish(NETWORK_FETCHER_ERROR.DONE,ASYNC_RES_STATE.SUCCESS);
    }

    public void setDebug(boolean s){
        debugMode=s;
    }

}
//{index}<name>或class=cls1,cls2或id={index}level2{index}level3

class PathParser{
    static ParsedPath parse(final String rawpath){
        if(rawpath.charAt(0)=='{'&&rawpath.charAt(1)=='R'){
            ParsedPath p=new ParsedPath();
            p.setRegex();
            p.add(rawpath.substring(3),0);
            return p;
        }//is regex
        ParsedPath path = new ParsedPath();
        String[] steps = rawpath.split("\\{");

        for (int i = 1; i < steps.length; i++) {
            String[] splited = steps[i].split("\\}");
            path.add(splited[1], Integer.parseInt(splited[0]));
        }
        return path;

    }
}

class ParsedPath{
    private List<PathElement> path=new ArrayList<>();
    private int ptr=0;
    private boolean regex=false;
    boolean isRegex(){
        return regex;
    }

    PathElement gotoHead(){
        ptr=0;
        return path.get(0);
    }
    PathElement getNext(){
        ptr++;
        if(ptr<path.size()) {
            return path.get(ptr);
        }else{
            return new PathElement(-1,"end");
        }
    }
    public void add(String name,int index){
        path.add(new PathElement(index,name));
    }
    void setRegex(){regex=true;}
}
class PathElement{
    public int index;
    public String name;
    PathElement(int index,String name){
        this.index=index;
        this.name=name;
    }
}



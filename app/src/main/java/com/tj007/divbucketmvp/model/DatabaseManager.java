package com.tj007.divbucketmvp.model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.components.treeview.TreeNode;
import com.tj007.divbucketmvp.model.persistent.HtmlFilterPath;
import com.tj007.divbucketmvp.model.persistent.HtmlHistory;
import com.tj007.divbucketmvp.model.persistent.URL;
import com.tj007.divbucketmvp.model.warpper.SimplifiedDomNode;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindCallback;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;


//TODO 缓存

public class DatabaseManager {
    private DatabaseManager(){
        SQLiteDatabase db = Connector.getDatabase();
        //?
    }
    private static DatabaseManager databaseManager=new DatabaseManager();

    public static DatabaseManager getInstance(){
        return databaseManager;
    }

    //目前这个方法配合着做一下activity之间传递数据的工作
    private List<List<TreeNode>> nodeCache=new ArrayList<>();
    private String urlCache;
    private String noteCache;
    private String clsCache;

    public void addSavePathCache(List<List<TreeNode>> nodes, String url){
        if(url=="")return;
        if(urlCache!=null){
            Log.e("ERROR", "addSavePathCache: still something in cache...." );
        }else {
            nodeCache.addAll(nodes);
            urlCache = url;
        }
    }

    public void clearSavePathCache(){
        urlCache=null;
        nodeCache.clear();
    }

    public void saveCache(String note,String cls){
        if(urlCache==null)return;
        saveURL(urlCache,note,cls);
        savePathAsync(urlCache,nodeCache);
    }

    public void savePathAsync(String url, final List<List<TreeNode>> path){
        LitePal.where("url=?",url).findFirstAsync(URL.class).listen(new FindCallback<URL>() {
            @Override
            public void onFinish(URL url) {
                if(url==null){
                    return;
                }
                String URL=url.getURL();
                List<HtmlFilterPath> paths=new ArrayList<>();
                for (List<TreeNode> p:path){
                    paths.add(ModelUtil.nodeClassesToPersisentFilterPath(p,URL));
                }
                LitePal.saveAllAsync(paths).listen(new SaveCallback() {
                    @Override
                    public void onFinish(boolean success) {

                    }
                });
            }
        });

    }

    public void saveURL(String url,String note,String cls){
        URL url1=new URL();
        url1.setActive(true);
        url1.setURL(url);
        url1.setNote(note);
        url1.setCls(cls);
        url1.save();
    }

    public void getAllPathInUrlAsync(final String url,AsyncResponse<List<String>> callback){
        URL URLo=LitePal.where("url=?",url).findFirst(URL.class);
        if(URLo==null){
            callback.processFinish(new ArrayList<>(),ASYNC_RES_STATE.ERROR);
            return;
        }
        LitePal.where("url=?",URLo.getURL()).findAsync(HtmlFilterPath.class).listen(new FindMultiCallback<HtmlFilterPath>() {
            @Override
            public void onFinish(List<HtmlFilterPath> list) {
                List<String> list1=new ArrayList<>();
                for (HtmlFilterPath p:list
                     ) {
                    list1.add(p.getPath());
                }
                callback.processFinish(list1,ASYNC_RES_STATE.SUCCESS);
            }
        });
    }

    /**
     *
     * @return
     */
    public void getAllActivePathAsync(AsyncResponse<Dictionary<String,List<String>>> callback){
        LitePal.where("isActive=?","1").findAsync(HtmlFilterPath.class).listen(new FindMultiCallback<HtmlFilterPath>() {
            @Override
            public void onFinish(List<HtmlFilterPath> list) {
                //<URL,path>
                Dictionary<String,List<String>> paths=new Hashtable<>();
                for (HtmlFilterPath p:list
                ) {
                    if(p.isActive()) {
                        List<String> container=paths.get(p.getUrl());
                        if(container!=null){
                            container.add(p.getPath());
                        }else {
                            List<String> list1=new ArrayList<>();
                            list1.add(p.getPath());
                            paths.put(p.getUrl(), list1);
                        }
                    }
                }
                callback.processFinish(paths, ASYNC_RES_STATE.SUCCESS);
            }
        });

    }

    public String hasURL(String url){
        URL u=LitePal.where("URL=?",url).findFirst(URL.class);
        if(u!=null){
            return u.getURL();
        }
        return null;
    }

    public List<String> getURL(String keyword,Integer num){
        List<String> result=new ArrayList<>();
        List<URL> URLs;
        if(keyword=="" ||keyword==null){
            URLs=LitePal.findAll(URL.class);
        }else{
            URLs=LitePal.where("url like ?",keyword).find(URL.class);
        }
        for (URL s:URLs
             ) {
            result.add(s.getURL());
        }
        return result;
    }
    public List<String> getPathInURL(String URL, String pathKeyword){
        List<String> result=new ArrayList<>();
        List<HtmlFilterPath> paths=new ArrayList<>();

        paths=LitePal.where("url=?",URL).find(HtmlFilterPath.class);

        for (HtmlFilterPath s:paths) {
            result.add(s.getPath());
        }
        return result;
    }
    public void writeNewWatchingResult(String path,String URL,String Result){
        HtmlHistory his=new HtmlHistory();
        HtmlHistory old=LitePal.where("URL=?",URL).findFirst(HtmlHistory.class);
        if(old==null){
            his.setDate(new Date());
            his.setPath(path);
            his.setCurData(Result);
            his.setOldData("");
            his.setURL(URL);
            his.save();
        }else{
            old.setOldData(old.getCurData());
            old.setCurData(Result);
            old.setDate(new Date());
            old.save();
        }
    }
    public List<String> getWatchingResult(String path,String URL){
        HtmlHistory h=LitePal.where("URL=?",URL).findFirst(HtmlHistory.class);
        if(h==null){
            return null;
        }
        List<String> list=new ArrayList<>();
        list.add(h.getCurData());
        list.add(h.getOldData());
        return list;
    }

    public ArrayList<String> getWatchingResultByPath(String path){
        return null;
    }
    public ArrayList<String> getOnlyHistroyWatchingResultByPath(String path){
        return null;
    }
}
class ModelUtil{
    public static String nodeClassesToStringPath(List<TreeNode> nodes){
        StringBuilder sb=new StringBuilder();

        for (int i=nodes.size()-1;i>=0;i--) {
            sb.append('{');
            sb.append(nodes.get(i).getIndex());
            sb.append('}');
            sb.append(((SimplifiedDomNode)nodes.get(i).getValue()).getType());
        }
        return sb.toString();
    }
    public static HtmlFilterPath nodeClassesToPersisentFilterPath(List<TreeNode> nodes,String targetUrl){
        HtmlFilterPath h=new HtmlFilterPath();
        h.setPath(nodeClassesToStringPath(nodes));
        h.setMode("raw");
        h.setUrl(targetUrl);
        return h;
    }
}
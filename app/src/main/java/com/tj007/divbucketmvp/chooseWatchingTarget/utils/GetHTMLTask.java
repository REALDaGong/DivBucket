package com.tj007.divbucketmvp.chooseWatchingTarget.utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE.*;


//https://stackoverflow.com/questions/6343166/how-do-i-fix-android-os-networkonmainthreadexception
//*仔细*阅读里面的*每一个*答案

/** 获得HTML.body的异步任务,传入1个URL。
 *  默认10000mill的超时时间
 */
public class GetHTMLTask extends AsyncTask<String,Void,Element>{

    /**
     *
     * @param delegate 回调方法，使用AsyncResponse接口
     */

    public GetHTMLTask(AsyncResponse delegate){
        this.delegate=delegate;
    }

    public AsyncResponse delegate = null;

    private Exception e;

    private ASYNC_RES_STATE netState=PENDING;

    @Override
    protected Element doInBackground(String... urls) {
        try {
            String URL=urls[0];
            Document doc= Jsoup.connect(URL).timeout(10000).get();
            netState=SUCCESS;
            return doc.body();
        }catch (Exception e){
            this.e=e;
            netState=ERROR;
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Element element){
        if (element==null){
            delegate.processFinish(null,ERROR);
        }
        delegate.processFinish(element,SUCCESS);
    }


}

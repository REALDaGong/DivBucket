package com.tj007.divbucketmvp.chooseWatchingTarget.utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE.*;


//https://stackoverflow.com/questions/6343166/how-do-i-fix-android-os-networkonmainthreadexception
//*仔细*阅读里面的*每一个*答案

/** 获得HTML.body的异步任务,传入1个URL。
 *  默认10000mill的超时时间
 *
 */
public class GetHTMLTask{

    /**
     *
     * @param delegate 回调方法，使用AsyncResponse接口
     */

    public GetHTMLTask(AsyncResponse delegate){
        callback=delegate;
    }

    public AsyncResponse callback = null;

    public void execute(String URL){
        Single.create((e)->{
            Document doc = Jsoup.connect(URL).timeout(10000).get();
            e.onSuccess(doc);
        }).subscribeOn(Schedulers.io())
                .subscribe(onSuccess-> {
                            callback.processFinish(onSuccess,SUCCESS);
                        },
                        onError->{
                            callback.processFailed(onError,ERROR);
                        });
    }

}

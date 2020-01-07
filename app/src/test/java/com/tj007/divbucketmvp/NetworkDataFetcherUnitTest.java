package com.tj007.divbucketmvp;

import com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.model.NetworkDataFetcher;

import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Test;

import java.io.Console;
import java.util.ArrayList;

public class NetworkDataFetcherUnitTest {
    @Test
    public void getHtmlTest(){
        NetworkDataFetcher networkDataFetcher=NetworkDataFetcher.getInstance();
        networkDataFetcher.getDataAsync("https://baidu.com",new AsyncResponse<Element>(){
            @Override
            public void processFinish(Element output, ASYNC_RES_STATE state) {
                assert output!=null;
                System.out.print(output);
            }
        });
    }

    @Test
    public void fetchDataTest(){
        System.out.println(Thread.currentThread().getName());
        NetworkDataFetcher networkDataFetcher=NetworkDataFetcher.getInstance();
        ArrayList<String> path=new ArrayList<>();
        path.add("{1}div{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        path.add("{0}shit{1}ok{2}div");
        networkDataFetcher.getDataAsync("https://baidu.com",path,new AsyncResponse<String>(){
            @Override
            public void processFinish(String output, ASYNC_RES_STATE state) {
                assert output!=null;
                System.out.println(output);
            }
        });
        System.out.println(Thread.currentThread().getName());
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(20000);
    }
}

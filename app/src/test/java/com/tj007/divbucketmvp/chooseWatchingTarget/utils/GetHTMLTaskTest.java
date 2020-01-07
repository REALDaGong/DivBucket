package com.tj007.divbucketmvp.chooseWatchingTarget.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetHTMLTaskTest {
    @Test
    public void testRx()throws InterruptedException{
        AsyncResponse<String> a=new AsyncResponse<String>() {
            @Override
            public void processFinish(String output, ASYNC_RES_STATE state) {
                System.out.println(output);
            }
        } ;
        System.out.println(12);
        GetHTMLTask task=new GetHTMLTask(a);
        task.execute("https://baidu.com");

        Thread.sleep(20000);

    }
}
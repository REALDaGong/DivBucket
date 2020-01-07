package com.tj007.divbucketmvp.model;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public final class ModelDebugHelper {
    static NetworkDataFetcher networkDataFetcher=NetworkDataFetcher.getInstance();
    static DatabaseManager databaseManager=DatabaseManager.getInstance();

    public static void setDebugHtml(boolean switc){
        networkDataFetcher.setDebug(switc);
    }

    public static  void clearDataBase(LitePalSupport modelClass){
        LitePal.deleteAll(modelClass.getClass());
    }
}

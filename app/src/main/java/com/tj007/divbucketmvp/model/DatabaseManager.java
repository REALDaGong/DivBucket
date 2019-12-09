package com.tj007.divbucketmvp.model;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.tablemanager.Connector;

public class DatabaseManager {
    private DatabaseManager(){
        SQLiteDatabase db = Connector.getDatabase();
        //?
    }
    private static DatabaseManager databaseManager=new DatabaseManager();

    public static DatabaseManager getInstance(){
        return databaseManager;
    }

    public void savePath(String URL,String path){

    }

    public void saveURL(String URL){

    }

    public void getAllPath(String URL){

    }

    public void hasURL(String URL){

    }

    public void getURL(String keyword,Integer num){

    }
    public void getPathInURL(String URL,String pathKeyword){

    }
}

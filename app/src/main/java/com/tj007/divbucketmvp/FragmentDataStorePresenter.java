package com.tj007.divbucketmvp;

import android.os.Bundle;

import java.util.HashMap;

//fragment之间传递一些小量数据用，不要乱用.
public class FragmentDataStorePresenter {
    private static HashMap<String,Bundle> bundles=new HashMap<>();

    public static void store(Bundle b,String key){
        bundles.put(key,b);
    }
    public static void clear(){
        bundles.clear();
    }
    public static Bundle read(String key){

        Bundle b= bundles.get(key);
        if(b!=null){
            bundles.remove(key);
            return b;
        }
        return null;
    }
}

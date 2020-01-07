package com.tj007.divbucketmvp.mailboxsystem.controls;

import android.app.Activity;
import android.widget.Toast;

import com.tj007.divbucketmvp.mailboxsystem.EmailApplication;


public class Controls {

    public static TitleBar getTitleBar() {
        return new TitleBar();
    }

    public static ProgressDialog getProgressDialog(Activity activity) {
        return new ProgressDialog(activity);
    }

    public static void toast(String s) {
        Toast.makeText(EmailApplication.getContext(), s, Toast.LENGTH_SHORT).show();
    }

}

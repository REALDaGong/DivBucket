package com.tj007.divbucketmvp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tj007.divbucketmvp.service.GTPushService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, GTPushService.class);
        context.startService(i);
    }
}

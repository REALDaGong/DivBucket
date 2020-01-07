package com.tj007.divbucketmvp.mailboxsystem.activity;

import android.content.Intent;
import android.os.Bundle;

import com.smailnet.emailkit.EmailKit;
import com.smailnet.microkv.MicroKV;
import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.mailboxsystem.controls.Controls;
import com.tj007.divbucketmvp.mailboxsystem.table.LocalMsg;

import org.litepal.LitePal;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void initView() {
        Controls.getTitleBar().display(this, "调试");

        findViewById(R.id.activity_settings_db_btn)
                .setOnClickListener(v -> {
                    LitePal.deleteAll(LocalMsg.class);
                    Controls.toast("已清除");
                });

        findViewById(R.id.activity_settings_sp_btn)
                .setOnClickListener(v -> {
                    MicroKV.customize("config", true).removeKV("folder_name");
                    Controls.toast("已清除");
                });

        findViewById(R.id.activity_settings_logout_btn)
                .setOnClickListener(v -> {
                    LitePal.deleteAll(LocalMsg.class);
                    MicroKV.customize("config").clear();
                    EmailKit.destroy();
                    Intent intent = new Intent(this, ConfigActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
    }

}

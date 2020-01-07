package com.tj007.divbucketmvp.mailboxsystem.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.mailboxsystem.adapter.item.MsgItem;

import java.util.List;

public class MsgAdapter extends BaseQuickAdapter<MsgItem, BaseViewHolder> {

    public MsgAdapter(@Nullable List<MsgItem> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgItem item) {
        helper.setText(R.id.item_message_nickname, item.getSenderNickname())
                .setText(R.id.item_message_subject, item.getSubject())
                .setText(R.id.item_message_date, item.getDate());
        ((TextView)helper.getView(R.id.item_message_nickname))
                .getPaint().setFakeBoldText(!item.isRead());
        ((TextView)helper.getView(R.id.item_message_subject))
                .getPaint().setFakeBoldText(!item.isRead());
        ((TextView)helper.getView(R.id.item_message_date))
                .getPaint().setFakeBoldText(!item.isRead());
    }

}

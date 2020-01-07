package com.tj007.divbucketmvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;
import com.tj007.divbucketmvp.contract.newWatchingTargetContract;
import com.tj007.divbucketmvp.model.warpper.watchingTargetWarpper;
import com.tj007.divbucketmvp.view.ChooseWatchingTargetActivity;

import java.util.ArrayList;

public class NewWatchingTargetView extends Fragment implements newWatchingTargetContract.View {

    private newWatchingTargetContract.Presenter mPresenter;

    private EditText urlField;
    private EditText nameField;
    private EditText classField;

    private ListView ruleList;
    private RuleAdapter adapter;
    private MaterialButton okButton;
    private MaterialButton addrule;

    private ArrayList<String> rules=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceBundle){
        View view = inflater.inflate(R.layout.addnew_watching_target,container,false);
        initView(view);
        Log.d("debug","ok.");
        return view;
    }


    @Override
    public void showLoading() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void hideLoading() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void attachPresenter(newWatchingTargetContract.Presenter presenter) {
        mPresenter=presenter;
    }

    private void initView(View view){
        urlField=view.findViewById(R.id.URLtext);
        nameField=view.findViewById((R.id.nametext));
        classField=view.findViewById(R.id.classtext);

        ruleList=view.findViewById(R.id.rulelist);
        okButton=view.findViewById(R.id.ok);
        addrule=view.findViewById(R.id.addrule);

        initRuleList();
        initButton();
    }

    private class RuleAdapter extends BaseAdapter{
        private Context context;

        public RuleAdapter(Context context){
            super();
            this.context=context;
        }
        @Override
        public int getCount() {
            return rules.size();
        }

        @Override
        public String getItem(int i) {
            return rules.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            String rule=getItem(i);
            View view;
            ViewHolder viewHolder;
            if(convertView == null){
                view=LayoutInflater.from(context).inflate(R.layout.component_default_list_cell,null);
                viewHolder=new ViewHolder();
                viewHolder.text=(TextView)view.findViewById(R.id.textView);
                view.setTag(viewHolder);
            }else{
                view=convertView;
                viewHolder=(ViewHolder) view.getTag();
            }
            viewHolder.text.setText(rule);
            return view;
        }
        class ViewHolder{
            TextView text;
        }
    }

    private void initRuleList(){
        adapter=new RuleAdapter(this.getContext());
        ruleList.setAdapter(adapter);
    }

    private int dp2Px(float dp){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5f);
    }

    private void setRuleListHeight(){

        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, ruleList);
            listItem.measure(0, 0);
            //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += dp2Px(listItem.getMeasuredHeight())+ruleList.getDividerHeight();
        }
        ViewGroup.LayoutParams params = ruleList.getLayoutParams();
        params.height = totalHeight; ruleList.setLayoutParams(params);
    }

    //called as callback
    public void addRuletoList(String rule){

        rules.add(rule);
        adapter.notifyDataSetChanged();
    }

    private void initButton(){
        Log.d("debug","lets init!");
        if(okButton!=null){
            Log.d("debug","okbutton inited");
            okButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Log.d("debug","ok.");
                    watchingTargetWarpper data=new watchingTargetWarpper();
                    data.setCls(getCls());
                    data.setName(getName());
                    data.setUrl(getUrl());
                    for (String rule:rules){
                        data.addRule(rule);
                    }
                    mPresenter.saveAll(data);
                    clearAll();
                    //jump back！！！！！！！！！！！！！！！
                }
            });
        }
        if(addrule!=null){

            addrule.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //此处跳转到选择界面
                    Intent intent=new Intent(getActivity(), ChooseWatchingTargetActivity.class);
                    intent.putExtra("url",getUrl());

                    startActivityForResult(intent,0);
                }
            });
        }
    }
    //从textview里格式化数据的工具方法
    private String getUrl(){
        if(urlField!=null){
            String rawString=urlField.getText().toString();
            return "https://www.".concat(rawString);
        }
        return "";
    }

    private String getName(){
        if(nameField!=null){
            String rawString=nameField.getText().toString();
            return rawString;
        }
        return "";
    }
    private String getCls(){
        if(classField!=null){
            String rawString=classField.getText().toString();
            return rawString;
        }
        return "";
    }

    private void clearAll(){
        classField.setText("");
        nameField.setText("");
        urlField.setText("");
        rules.clear();
        adapter.notifyDataSetChanged();
    }

    public void addPath(String[] s){
        for (String ss:s
             ) {
            addRuletoList(ss);
        }
    }
}

package com.tj007.divbucketmvp.view;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.tj007.divbucketmvp.R;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.ASYNC_RES_STATE;
import com.tj007.divbucketmvp.chooseWatchingTarget.utils.AsyncResponse;
import com.tj007.divbucketmvp.contract.showWatchingTargetContract;
import com.tj007.divbucketmvp.model.warpper.ListData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowWatchingTargetView extends Fragment implements showWatchingTargetContract.View {


    private SearchView mSearchView;
    private RecyclerView mRecylerView;
    private InnerAdapter mAdapter;
    private showWatchingTargetContract.Presenter mPresenter;
    private List<ListData> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_list_fragment,container,false);
        initView(view);
        return view;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_show,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                updateAll();
                return true;
        }
        return false;
    }

    @Override
    public void updateAll() {
        mPresenter.updateAll(new AsyncResponse() {
            @Override
            public void processFinish(Object output, ASYNC_RES_STATE state) {

            }
        });
    }

    private void refreshRV(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateViewOnly(){
        getActivity().runOnUiThread(this::refreshRV);
    }

    @Override
    public void attachPresenter(showWatchingTargetContract.Presenter presenter) {
        this.mPresenter=presenter;
    }

    private void initView(View view){
        //mSearchView=view.findViewById(R.id.searchbar);
        mRecylerView=view.findViewById(R.id.recyclerView);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter=new InnerAdapter(getContext(),mPresenter.getListData());
        mAdapter.setOnDelListener(new InnerAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                Toast.makeText(getContext(), "删除:" + pos, Toast.LENGTH_SHORT).show();
                data.remove(pos);
                mAdapter.notifyItemRemoved(pos);//推荐用这个
                //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                //mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTop(int pos) {

            }
        });
        mRecylerView.setAdapter(mAdapter);
    }

}
class InnerViewHolder extends RecyclerView.ViewHolder{
    //@BindView(R.id.node_container)
    LinearLayout layout;
    //@BindView(R.id.node_name_view)
    TextView name;
    //@BindView(R.id.node_content_view)
    TextView content;
    //@BindView(R.id.node_date_view)
    TextView date;
    //@BindView(R.id.btnDelete)
    Button btnDelete;



    public InnerViewHolder(View itemView){
        super(itemView);
        //ButterKnife.bind(itemView);
        content = itemView.findViewById(R.id.node_content_view);
        btnDelete = itemView.findViewById(R.id.btnDelete);
        name = itemView.findViewById(R.id.node_name_view);
        date = itemView.findViewById(R.id.node_date_view);
        layout= itemView.findViewById(R.id.node_container);

    }
}
class InnerAdapter extends RecyclerView.Adapter<InnerViewHolder>{

    private Context mContext;
    private LayoutInflater mInflater;
    List<ListData> data;
    public InnerAdapter(Context context,List<ListData> data){
        this.data=data;
        this.mContext=context;
        mInflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InnerViewHolder(mInflater.inflate(R.layout.update_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        ((SwipeMenuLayout) holder.itemView).setLeftSwipe(true);
        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑

        holder.name.setText(data.get(position).name);
        holder.content.setText(data.get(position).msg);
        holder.date.setText(data.get(position).lastUpdateTime.toString());

        (holder.layout).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "onClick:" + data.get(holder.getAdapterPosition()).name, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClick() called with: v = [" + view + "]");
                Intent intent=new Intent(mContext, ShowDetailActivity.class);
                intent.putExtra("msg",data.get(position).msg);
                intent.putExtra("url",data.get(position).name);
                intent.putExtra("time",data.get(position).lastUpdateTime);
                mContext.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=mOnSwipeListener){
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });
    }
    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener){
        this.mOnSwipeListener=mOnDelListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 通信接口
     */

    public interface onSwipeListener{
        void onDel(int pos);
        void onTop(int pos);
    }
}
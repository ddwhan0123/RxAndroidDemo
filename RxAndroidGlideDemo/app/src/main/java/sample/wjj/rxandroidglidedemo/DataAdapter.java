package sample.wjj.rxandroidglidedemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import sample.wjj.rxandroidglidedemo.OBJ.TestOBJ;

/**
 * Created by jiajiewang on 16/3/25.
 */
public class DataAdapter extends RecyclerView.Adapter {
    Context context;
    List<TestOBJ> testOBJs;

    public DataAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DataViewHolder dataViewHolder = (DataViewHolder) holder;
        TestOBJ obj = testOBJs.get(position);
        Glide.with(holder.itemView.getContext()).load(obj.imageUrl).into(dataViewHolder.imageIV);
        dataViewHolder.contentTV.setText(obj.content);
    }

    @Override
    public int getItemCount() {
        return testOBJs == null ? 0 : testOBJs.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIV;
        TextView contentTV;

        public DataViewHolder(View itemView) {
            super(itemView);
            imageIV = (ImageView) itemView.findViewById(R.id.imageIV);
            contentTV = (TextView) itemView.findViewById(R.id.contentTV);
        }
    }

    //刷新数据用
    public void updateData(List<TestOBJ> testOBJs) {
        this.testOBJs = testOBJs;
        notifyDataSetChanged();
    }
}

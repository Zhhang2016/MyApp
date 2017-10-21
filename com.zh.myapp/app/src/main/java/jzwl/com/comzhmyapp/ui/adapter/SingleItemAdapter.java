package jzwl.com.comzhmyapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jzwl.com.comzhmyapp.R;

/**
 * 创建日期：2017/10/17
 * 描述: 单条目的 RecycelrView
 *
 * @author: zhaoh
 */
public class SingleItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> mList;

    public SingleItemAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(mContext, R.layout.layout_refresh_item, null);

        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        if (mList != null && mList.size() > 0) {
            String name = mList.get(position);
            viewHolder.tvName.setText(name);
        }
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}

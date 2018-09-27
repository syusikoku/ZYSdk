package com.zysdk.vulture.clib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.bean.BannerData;
import com.zysdk.vulture.clib.glide.GlideUtils;
import com.zysdk.vulture.clib.utils.UiUtils;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    private final List<BannerData> mData;
    private final LayoutInflater layoutInflater;
    private OnItemClickListener mOnIemClickListener;

    public BannerAdapter(List<BannerData> list) {
        this.mData = list;
        layoutInflater = LayoutInflater.from(UiUtils.getContext());
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_banner_item, parent, false);
        return new BannerViewHolder(view);
    }

    public interface OnItemClickListener {
        void onIemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnIemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        BannerData data = mData.get(position % mData.size());
        GlideUtils.loadPic(UiUtils.getAppInstance(), data.getImgUrl(), holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            if (mOnIemClickListener != null) {
                mOnIemClickListener.onIemClick(position % mData.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size() < 2 ? mData.size() : Integer.MAX_VALUE;
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public BannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_banner_iv);
        }
    }
}
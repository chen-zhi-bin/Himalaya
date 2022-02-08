package com.program.himalaya.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.program.himalaya.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.InnerHolder> {

    private static final String TAG = "RecommendListAdapter";
    private List<Album> mdata = new ArrayList<>();
    private OnAlbumItemClickListener mItemClickListener = null;
    private OnAlbumItemLongClickListener mLongClickListener = null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //这里是载view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //这里是设置数据
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    int clickPostion = (int) v.getTag();
                    mItemClickListener.onItemClick(clickPostion, mdata.get(clickPostion));
                }
                Log.d("RecommendLostAdapter", "holder.itemView click -->" + v.getTag());
            }
        });
        holder.setData(mdata.get(position));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongClickListener != null) {
                    int clickPostion = (int) v.getTag();
                    mLongClickListener.onItemLongClick(mdata.get(clickPostion));
                }
                //true表示消费掉该事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        //返回要显示的个数
        if (mdata != null) {
            return mdata.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (mdata != null) {
            mdata.clear();
            mdata.addAll(albumList);
        }
        //更新UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Album album) {
            //找到控件，设置数据
            //专辑封面      itemView在ViewHolder类中public
            ImageView albumCoverIv = itemView.findViewById(R.id.album_cover);
            //title
            TextView albumTitleTv = itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView albumDesTv = itemView.findViewById(R.id.album_description_tv);
            //播放数量
            TextView albumPlayCountTv = itemView.findViewById(R.id.album__play_count);
            //专辑内容数量
            TextView albumContenCountTv = itemView.findViewById(R.id.album_content_size);

            //加载数据
            albumTitleTv.setText(album.getAlbumTitle());
            albumDesTv.setText(album.getAlbumIntro());
            albumPlayCountTv.setText(album.getPlayCount() + "");
            albumContenCountTv.setText(album.getIncludeTrackCount() + "");
            //加载图片
            String coverUrlLarge = album.getCoverUrlLarge();
            if (!TextUtils.isEmpty(coverUrlLarge)){
                Picasso.with(itemView.getContext()).load(coverUrlLarge).into(albumCoverIv);
            }else {
                albumCoverIv.setImageResource(R.mipmap.logo);
            }

//            if (album.getCoverUrlLarge()!=null){
//                Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);
//            }
        }
    }

    public void setAlbumItemClickListener(OnAlbumItemClickListener listner) {
        this.mItemClickListener = listner;
    }

    public interface OnAlbumItemClickListener {
        void onItemClick(int position, Album album);
    }

    public void setOnAlbumItemLongClickListener(OnAlbumItemLongClickListener listener){
        this.mLongClickListener = listener;
    }

    /**
     * 长按的接口
     */
    public interface OnAlbumItemLongClickListener{
        void onItemLongClick(Album album);
    }
}

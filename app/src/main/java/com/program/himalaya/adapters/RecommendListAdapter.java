package com.program.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bun.miitmdid.interfaces.InnerIdProvider;
import com.program.himalaya.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.InnerHolder> {
    private List<Album> mdata =new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //这里是载view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //这里是设置数据
        holder.itemView.setTag(position);
        holder.setData(mdata.get(position));
    }

    @Override
    public int getItemCount() {
        //返回要显示的个数
        if (mdata!=null){
            return mdata.size();
        }
        return 0;
    }

    public void setData(List<Album> albumList) {
        if (mdata!=null){
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
            ImageView albumCoverIv=itemView.findViewById(R.id.album_cover);
            //title
            TextView albumTitleTv=itemView.findViewById(R.id.album_title_tv);
            //描述
            TextView albumDesTv = itemView.findViewById(R.id.album_description_tv);
            //播放数量
            TextView albumPlayCountTv =itemView.findViewById(R.id.album__play_count);
            //专辑内容数量
            TextView albumContenCountTv=itemView.findViewById(R.id.album_content_size);

            //加载数据
            albumTitleTv.setText(album.getAlbumTitle());
            albumDesTv.setText(album.getAlbumIntro());
            albumPlayCountTv.setText(album.getPlayCount()+"");
            albumContenCountTv.setText(album.getIncludeTrackCount()+"");
            //加载图片
            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCoverIv);
        }
    }
}

package com.example.hacks17;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.*;

/**
 * Created by akshat on 29/10/17.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {
    private List<AudioModel> songsList;
    private MediaPlayer mPlayer;
    private Context context;
    private String userName;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.songname);

        }
    }

    public void userName(String userName) {
        this.userName = userName;
    }

    public SongsAdapter(List<AudioModel> moviesList, Context context,String userName) {
        mPlayer = new MediaPlayer();
        this.songsList = moviesList;
        this.context = context;
        this.userName = userName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_container, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AudioModel songMap = songsList.get(position);
        holder.title.setText(songMap.getaArtist());
        setClick(holder,songMap.getaAlbum(),songMap.getaArtist());
    }

    private void setClick(MyViewHolder holder,String path,final String name) {
        final String mPath = path;
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer = new MediaPlayer();
                try { mPlayer.setDataSource(mPath); } catch (Exception e) {}
                try { mPlayer.prepare(); } catch (Exception e) {}
                Long timestamp = System.currentTimeMillis();
                mPlayer.start();
                new SyncInit().send(timestamp,name,userName,context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }
}
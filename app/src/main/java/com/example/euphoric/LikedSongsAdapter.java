package com.example.euphoric;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LikedSongsAdapter extends ArrayAdapter<SongList> {

    public LikedSongsAdapter(Activity context , ArrayList<SongList> word){
        super(context,0,word);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.liked_songs_view,parent,false);
        }

        SongList cur = getItem(position);

        String name , artist, album , duration;
        String[] genres;
        name = cur.mSongName;
        artist = cur.mArtist;
        album = cur.mAlbum;
        duration =  cur.mDuration;
        genres = cur.getmSongGenres();

        TextView nameText = view.findViewById(R.id.song_name);
        nameText.setText(name);

        TextView artistText = view.findViewById(R.id.song_artist);
        artistText.setText(artist);

        TextView albumText = view.findViewById(R.id.song_album);
        albumText.setText(album);

        TextView durationText = view.findViewById(R.id.song_duration);
        durationText.setText(duration);

        TextView genresText = view.findViewById(R.id.song_genres);
        genresText.setText(genres.toString());

        return view;
    }
}

package com.example.euphoric.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.euphoric.R;
import com.example.euphoric.models.Song;
import com.example.euphoric.models.SpotifySong;

import java.util.ArrayList;
import java.util.Arrays;

public class LikedSongsAdapter extends ArrayAdapter<SpotifySong> {

    public LikedSongsAdapter(Activity context , ArrayList<SpotifySong> word){
        super(context,0,word);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.liked_songs_view,parent,false);
        }

        SpotifySong cur = getItem(position);

        String name , artist, album , duration;
        String[] genres;
        name = cur.getName();
        artist = cur.getArtist();
        album = cur.getAlbum();
        duration =  "20 sec";
//        genres = ["sdf"];

        TextView nameText = view.findViewById(R.id.song_name);
        nameText.setText(name);

        TextView artistText = view.findViewById(R.id.song_artist);
        artistText.setText(artist);

        TextView albumText = view.findViewById(R.id.song_album);
        albumText.setText("Album: " + album);

        TextView durationText = view.findViewById(R.id.song_duration);
        durationText.setText("Duration: " + duration);

        TextView genresText = view.findViewById(R.id.song_genres);
//        genresText.setText("Genres: " + Arrays.toString(genres));

        return view;
    }
}

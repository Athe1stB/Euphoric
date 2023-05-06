package com.example.euphoric.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.services.FirestoreService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LikedSongsAdapter extends ArrayAdapter<SpotifySong> {

    String callerType;

    public LikedSongsAdapter(Activity context, ArrayList<SpotifySong> word, String callerType) {
        super(context, 0, word);
        this.callerType = callerType;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.liked_songs_view, parent, false);
        }

        final Context applicationContext = view.getContext();

        SpotifySong cur = getItem(position);

        String id, name, artist, album, duration, uri;
        String[] genres;

        id = cur.getId();
        name = cur.getName();
        artist = cur.getArtist();
        album = cur.getAlbum();
        duration = "20 sec";
        uri = cur.getUri();
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

        Button likeButton = view.findViewById(R.id.song_like_dislike);
        if (!callerType.equals("search"))
            likeButton.setText("Dislike");
        else
            likeButton.setText("Like");

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!callerType.equals("search")) {
                    FirestoreService.deleteArrayElement("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "songIds", id);
                    remove(cur);
                    notifyDataSetChanged();
                } else
                    FirestoreService.addArrayElement("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "songIds", id);
            }
        });

        Button playButton = view.findViewById(R.id.song_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applicationContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });

        return view;
    }
}

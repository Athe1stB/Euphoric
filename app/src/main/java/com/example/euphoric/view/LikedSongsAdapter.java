package com.example.euphoric.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.MyBounceInterpolator;
import com.example.euphoric.services.SpotifyTracksService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LikedSongsAdapter extends ArrayAdapter<SpotifySong> {

    String callerType;
    ArrayList<String> getNewSongs = new ArrayList<>();

    public LikedSongsAdapter(Activity context, ArrayList<SpotifySong> word, String callerType) {
        super(context, 0, word);
        this.callerType = callerType;
        getNewSongs.add("search");
        getNewSongs.add("recommendations");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.liked_songs_view, parent, false);
        }

        final Context applicationContext = view.getContext();

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

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
        if (!getNewSongs.contains(callerType))
            likeButton.setText("Dislike");
        else
            likeButton.setText("Like");

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButton.startAnimation(myAnim);
                if (!getNewSongs.contains(callerType))
                    new DeleteSong(id, cur).execute();
                 else
                    new AddSong(id).execute();
            }
        });

        Button playButton = view.findViewById(R.id.song_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.startAnimation(myAnim);
                applicationContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });

        return view;
    }

    private class AddSong extends AsyncTask<Void, Void, Void> {
        private final String id;
        public AddSong(String id){
            super();
            this.id = id;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            FirestoreService.addArrayElement("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "songIds", id);return null;
        }
    }


    private class DeleteSong extends AsyncTask<Void, Void, Void> {
        private final String id;
        private final SpotifySong cur;
        public DeleteSong(String id, SpotifySong cur){
            super();
            this.id = id;
            this.cur = cur;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            remove(cur);
            notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            FirestoreService.deleteArrayElement("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "songIds", id);
            return null;
        }
    }
}

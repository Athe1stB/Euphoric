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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.MyBounceInterpolator;
import com.example.euphoric.services.SpotifyTracksService;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LikedSongsAdapter extends ArrayAdapter<SpotifySong> {

    String callerType;
    ArrayList<String> getNewSongs = new ArrayList<>();
    ArrayList<Boolean> likedStatus = new ArrayList<>();

    public LikedSongsAdapter(Activity context, ArrayList<SpotifySong> word, String callerType) {
        super(context, 0, word);
        this.callerType = callerType;
        getNewSongs.add("search");
        getNewSongs.add("recommendations");
        for (int i = 0; i < word.size(); i++)
            likedStatus.add((!getNewSongs.contains(callerType)));
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

        String id, name, artist, album, uri, thumbnail;
        long duration = cur.getDuration() / 1000;
        id = cur.getId();
        name = cur.getName();
        artist = cur.getArtist();
        album = cur.getAlbum();
        uri = cur.getUri();
        thumbnail = cur.getThumbnail();

        String durationStr = String.format("%02d:%02d", duration / 60, duration % 60);

        TextView nameText = view.findViewById(R.id.song_name);
        nameText.setText(name);

        TextView artistText = view.findViewById(R.id.song_artist);
        artistText.setText(artist);

        ImageButton playButton = view.findViewById(R.id.song_play);
        ImageButton likeButton = view.findViewById(R.id.song_like);
        ImageButton moreButton = view.findViewById(R.id.song_details);
        ImageView thumbnailImg = view.findViewById(R.id.song_thumbnail);
        Picasso.get().load(thumbnail).into(thumbnailImg);

        if (likedStatus.get(position))
            likeButton.setImageResource(R.drawable.like_filled);
        else
            likeButton.setImageResource(R.drawable.like_border);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButton.startAnimation(myAnim);
                if (likedStatus.get(position)) {
                    likeButton.setImageResource(R.drawable.like_border);
                        new DeleteSong(id, cur, !getNewSongs.contains(callerType)).execute();
                } else {
                    likeButton.setImageResource(R.drawable.like_filled);
                    new AddSong(id).execute();
                }
                likedStatus.set(position, !(likedStatus.get(position)));
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.startAnimation(myAnim);
                applicationContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreButton.startAnimation(myAnim);
            }
        });

        return view;
    }

    private class AddSong extends AsyncTask<Void, Void, Void> {
        private final String id;

        public AddSong(String id) {
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
            FirestoreService.addArrayElement("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "songIds", id);
            return null;
        }
    }


    private class DeleteSong extends AsyncTask<Void, Void, Void> {
        private final String id;
        private final SpotifySong cur;
        private final boolean deleteFromView;

        public DeleteSong(String id, SpotifySong cur, boolean deleteFromView) {
            super();
            this.id = id;
            this.cur = cur;
            this.deleteFromView = deleteFromView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(deleteFromView) {
                remove(cur);
                notifyDataSetChanged();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            FirestoreService.deleteArrayElement("Users", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "songIds", id);
            return null;
        }
    }
}

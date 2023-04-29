package com.example.euphoric.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.euphoric.R;
import com.example.euphoric.models.SongList;
import com.example.euphoric.models.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class VideoAdapter extends ArrayAdapter<Video> {

    public VideoAdapter(Activity context, ArrayList<Video> word) {
        super(context, 0, word);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.video_list_view, parent, false);
        }

        Video cur = getItem(position);

        String id, title, channelId, publishedAt, thumbnail, channelTitle, description, duration;

        id = cur.id;
        channelId = cur.channelId;
        title = cur.title;
        thumbnail = cur.thumbnail;
        channelTitle = cur.channelTitle;
        description = cur.description;
        publishedAt = cur.publishedAt;

        TextView titleText = view.findViewById(R.id.video_title);
        titleText.setText(title);

        TextView channelText = view.findViewById(R.id.video_channel);
        channelText.setText(channelTitle);

        TextView descriptionText = view.findViewById(R.id.video_description);
        descriptionText.setText(description);

        TextView publishedText = view.findViewById(R.id.video_published);
        publishedText.setText(publishedAt);

        TextView playButton = view.findViewById(R.id.video_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://youtu.be/"+id));
                v.getContext().startActivity(webIntent);
            }
        });

        ImageView thumbnailImage = view.findViewById(R.id.video_thumbnail);
        Picasso.get().load(thumbnail).into(thumbnailImage);
        return view;
    }

}

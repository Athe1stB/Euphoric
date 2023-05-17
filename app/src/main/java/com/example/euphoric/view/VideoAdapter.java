package com.example.euphoric.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.euphoric.R;
import com.example.euphoric.models.Video;
import com.example.euphoric.services.MyBounceInterpolator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

        final Context applicationContext = view.getContext();

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

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

        ImageButton playButton = view.findViewById(R.id.video_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playButton.startAnimation(myAnim);
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://youtu.be/" + id));
                v.getContext().startActivity(webIntent);
            }
        });

        ImageView thumbnailImage = view.findViewById(R.id.video_thumbnail);
        Picasso.get().load(thumbnail).into(thumbnailImage);

        ImageButton moreButton = view.findViewById(R.id.video_details);
        handleSpinnerDropdown(
                moreButton,
                applicationContext,
                title,
                channelTitle,
                thumbnail,
                description,
                publishedAt
        );
        return view;
    }


    private void handleSpinnerDropdown(
            ImageButton moreButton,
            Context applicationContext,
            String title,
            String channelTitle,
            String thumbnailUri,
            String description,
            String publishedAt
    ) {
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(applicationContext);
                dialog.setContentView(R.layout.video_details);
                dialog.getWindow().setLayout(1000, 900);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                TextView titleText = dialog.findViewById(R.id.video_title);
                titleText.setText(title);

                TextView channelText = dialog.findViewById(R.id.video_channel);
                channelText.setText(channelTitle);

                TextView descriptionText = dialog.findViewById(R.id.video_description);
                descriptionText.setText(description);

                TextView published = dialog.findViewById(R.id.video_published);
                published.setText(publishedAt);

                ImageView thumbnail = dialog.findViewById(R.id.video_thumbnail);
                Picasso.get().load(thumbnailUri).into(thumbnail);

            }
        });

    }
}

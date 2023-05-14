package com.example.euphoric.view;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.euphoric.MainActivity;
import com.example.euphoric.R;
import com.example.euphoric.models.SpotifySong;
import com.example.euphoric.services.FirestoreService;
import com.example.euphoric.services.MyBounceInterpolator;
import com.example.euphoric.services.SpotifyTracksService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    TextView name;
    TextView email;
    TextView phone;
    TextView dob;
    Map<String, Object> user;
    AsyncTask fetchUserDetailsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = this.getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        //defining animation
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
        myAnim.setInterpolator(interpolator);

        name = findViewById((R.id.user_profile_name));
        name.setText("Loading...");

        email = findViewById((R.id.user_email));
        email.setText("Loading...");

        phone = findViewById((R.id.user_phone));
        phone.setText("Loading...");

        dob = findViewById((R.id.user_dob));
        dob.setText("Loading...");

        final TextView likedSongsButton = findViewById(R.id.liked_songs_profile);
        likedSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likedSongsButton.startAnimation(myAnim);
                new GoToLikedSongs().execute();
            }
        });

        final TextView deleteAccount = findViewById(R.id.delete_account);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount.startAnimation(myAnim);
                Intent i = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                i.putExtra("operation", "delete account");
                startActivity(i);
            }
        });

        final TextView updatePassword = findViewById(R.id.update_password);
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword.startAnimation(myAnim);
                Intent i = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                i.putExtra("operation", "update password");
                startActivity(i);
            }
        });

        final TextView updateProfileButton = findViewById(R.id.update_profile);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileButton.startAnimation(myAnim);
                new NavigateToUpdateProfile().execute();
            }
        });

        final TextView signOutButton = findViewById(R.id.sign_out_profile);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutButton.startAnimation(myAnim);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserDetailsTask = new FetchUserDetails().execute();
    }

    private class FetchUserDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            name.setText(Objects.requireNonNull(user.get("name")).toString());
            email.setText(Objects.requireNonNull(user.get("email")).toString());
            phone.setText(Objects.requireNonNull(user.get("phone")).toString());
            try {
                dob.setText(
                        getDiffYears(
                                new SimpleDateFormat("dd/MM/yyyy").parse((String) user.get("dob")),
                                new Date()
                        )
                        + " yrs"
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            assert firebaseUser != null;
            user = FirestoreService.get("Users", firebaseUser.getEmail());
            return null;
        }
    }

    private class GoToLikedSongs extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<String> songIds = (ArrayList<String>) user.get("songIds");
            SpotifyTracksService ss = new SpotifyTracksService(requestQueue, sharedPreferences);
            ss.getTracksWithTrackIds(() -> {
                ArrayList<SpotifySong> songs = ss.getSongs();
                Intent i = new Intent(getApplicationContext(), LikedSongsActivity.class);
                i.putExtra("SongList", songs);
                i.putExtra("caller_type", "Profile");
                i.putExtra("contains_songs", songs.size() > 0);
                i.putExtra("error_msg", getResources().getString(R.string.no_liked_songs));
                startActivity(i);
            }, songIds);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (user == null) ;
            return null;
        }
    }

    private class NavigateToUpdateProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(getApplicationContext(), UpdateProfileActivity.class);
            i.putExtra("email", (String) (user.get("email")));
            i.putExtra("name", (String) (user.get("name")));
            i.putExtra("phone", (String) (user.get("phone")));
            i.putExtra("dob", (String) (user.get("dob")));
            startActivity(i);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (!fetchUserDetailsTask.getStatus().equals(Status.FINISHED)) ;
            return null;
        }
    }

    private static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

}
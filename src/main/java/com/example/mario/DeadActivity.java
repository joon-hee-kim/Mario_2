package com.example.mario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class DeadActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private VideoView videoView2;
    private MediaController mediaController;
    private String videoURL = "android.resource://com.example.mario/raw/mario_game_over";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dead);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        try {
            videoView2 = findViewById(R.id.videoView2);
            if (videoView2 != null) {
                videoView2.setOnCompletionListener(this);
//                mediaController = new MediaController(this);
//                mediaController.setAnchorView(videoView2);
                Uri uri = Uri.parse(videoURL);
//                videoView2.setMediaController(mediaController);
                videoView2.setVideoURI(uri);
                videoView2.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent startIntent = new Intent(DeadActivity.this, WldActivity.class);
        startActivity(startIntent);
    }

}
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


public class EndActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private VideoView videoView;
    private MediaController mediaController;
    private String videoURL = "android.resource://com.example.mario/raw/mario_clear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        try {
            videoView = findViewById(R.id.videoView);
            if (videoView != null) {
                videoView.setOnCompletionListener(this);
//                mediaController = new MediaController(this);
//                mediaController.setAnchorView(videoView);
                Uri uri = Uri.parse(videoURL);
//                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent startIntent = new Intent(EndActivity.this, MainActivity.class);
        startActivity(startIntent);
    }

}
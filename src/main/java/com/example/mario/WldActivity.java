package com.example.mario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class WldActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wld);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mediaPlayer = MediaPlayer.create(this, R.raw.wld);
        mediaPlayer.setLooping(true);

        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);

        if (button5 == null) return; //주니어
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.mario_start);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent playIntent = new Intent(WldActivity.this, PlayActivity.class);
                WldActivity.this.startActivity(playIntent);
            }
        });

        if (button6 == null) return; //쿠파
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.before_fight);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.start();
                Intent play2Intent = new Intent(WldActivity.this, Play2Activity.class);
                WldActivity.this.startActivity(play2Intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }


}
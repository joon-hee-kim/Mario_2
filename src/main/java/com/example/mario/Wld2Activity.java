package com.example.mario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Wld2Activity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wld2);
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
                Intent play3Intent = new Intent(Wld2Activity.this, Play3Activity.class);
                Wld2Activity.this.startActivity(play3Intent);
            }
        });

        if (button6 == null) return; //쿠파
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.before_fight);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.start();
                Intent play4Intent = new Intent(Wld2Activity.this, Play4Activity.class);
                Wld2Activity.this.startActivity(play4Intent);
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
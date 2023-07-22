package com.example.mario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SelActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sel);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        mediaPlayer = MediaPlayer.create(this, R.raw.main);
        mediaPlayer.setLooping(true);

        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);

        if (button3 == null) return; //마리오
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.me_mario);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent wldIntent = new Intent(SelActivity.this, WldActivity.class);
                SelActivity.this.startActivity(wldIntent);
            }
        });

        if (button4 == null) return; //루이지
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.me_luigi);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.start();
                Intent wld2Intent = new Intent(SelActivity.this, Wld2Activity.class);
                SelActivity.this.startActivity(wld2Intent);
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
package com.example.mario;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        AnimationDrawable marioAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.animate);
        AnimationDrawable luigiAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.animate2);

        // ImageView에 애니메이션 설정
        ImageView marioImage = (ImageView) findViewById(R.id.character07);
        if (marioImage != null) {
            marioImage.setImageDrawable(marioAnimation);
        }

        ImageView luigiImage = (ImageView) findViewById(R.id.luigi07);
        if (luigiImage != null) {
            luigiImage.setImageDrawable(luigiAnimation);
        }

        marioAnimation.start();
        luigiAnimation.start();

        mediaPlayer = MediaPlayer.create(this, R.raw.title);
        mediaPlayer.setLooping(true);

        TextView textView2 = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);


        if (button == null) return;
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.mario_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                Intent selIntent = new Intent(MainActivity.this, SelActivity.class);
                MainActivity.this.startActivity(selIntent);
            }
        });

        if (button2 == null) return;
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.mario_start);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.start();
                Intent selIntent = new Intent(MainActivity.this, SelActivity.class);
                MainActivity.this.startActivity(selIntent);
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

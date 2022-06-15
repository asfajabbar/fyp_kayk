package com.example.project_kayk.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_kayk.R;
import com.example.project_kayk.utils.SingletonClass;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView splashImage;
    TextView logo, slogan;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // getSupportActionBar().hide();
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        splashImage = findViewById(R.id.splashimage);
        logo = findViewById(R.id.app_name);
        slogan = findViewById(R.id.tagline);
        splashImage.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, loginActivity.class);
                startActivity(intent);
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        }, 4000);

    }

    @Override
    protected void onResume() {
        super.onResume();

        SingletonClass.totalPrice = 0;

        SingletonClass.totalPrice = 0;
        SingletonClass.spongePrice = 0;
        SingletonClass.fillingPrice = 0;
        SingletonClass.icingPrice = 0;
        SingletonClass.garnishPrice = 0;
        SingletonClass.layerPrice = 0;
        SingletonClass.tierPrice = 0;


    }
}


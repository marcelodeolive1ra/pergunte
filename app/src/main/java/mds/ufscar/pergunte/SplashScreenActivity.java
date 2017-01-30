package mds.ufscar.pergunte;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        int SPLASH_TIME_OUT = 2000;

        Toolbar splashScreenToolbar = (Toolbar)findViewById(R.id.splash_scrren_toolbar);
        splashScreenToolbar.setTitle("Pergunte");
        splashScreenToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

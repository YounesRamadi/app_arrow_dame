package activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

public class MainActivity extends AppCompatActivity {

    private TextView mGameName;
    private ImageView mLogo;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGameName = (TextView) findViewById(R.id.nomjeu);
        mLogo = (ImageView) findViewById(R.id.etoile);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
<<<<<<<< HEAD:app/src/main/java/activities/MainActivity.java
        }, 10);
========
        },1000);
>>>>>>>> ae12517 (merge branches ia et antonin):app/src/main/java/controller/MainActivity.java
    }
}
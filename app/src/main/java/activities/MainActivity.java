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

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, DisplayBoardActivity.class));
                finish();
            }
        }, 10);
    }
}
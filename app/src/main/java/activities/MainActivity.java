package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import controller.User;

public class MainActivity extends AppCompatActivity {

    private TextView mGameName;
    private ImageView mLogo;
    public User mUser = new User();
    private SharedPreferences preferences;

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
                preferences = getSharedPreferences("firstname", MODE_PRIVATE);
                String firstname = preferences.getString("firstname", null);
                if (firstname !=null) {
                    mUser.setFirstName(firstname);
                    startActivity(new Intent(MainActivity.this, MenuActivity.class));
                }
                else{
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 10);
    }
}
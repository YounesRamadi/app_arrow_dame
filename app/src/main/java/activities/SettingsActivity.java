package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apadnom.R;

import controller.User;

public class SettingsActivity extends AppCompatActivity {

    private EditText mChangeName;
    public User mUser = new User();
    private SharedPreferences preferences;
    private TextView mcredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mcredits = (TextView) findViewById(R.id.credits);
        mChangeName = (EditText) findViewById(R.id.nomedit);

        preferences = getSharedPreferences("firstname", MODE_PRIVATE);
        String firstname = preferences.getString("firstname", null);
        mUser.setFirstName(firstname);

        mChangeName.setText(firstname);

        mcredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri myURI = Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
                Intent i = new Intent(Intent.ACTION_VIEW, myURI);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        mUser.setFirstName(mChangeName.getText().toString());
        preferences = getSharedPreferences("firstname", MODE_PRIVATE);
        preferences.edit().putString("firstname", mChangeName.getText().toString()).apply();
    }


}
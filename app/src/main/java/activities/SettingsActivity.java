package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.example.apadnom.R;

import controller.User;

public class SettingsActivity extends AppCompatActivity {

    private EditText mChangeName;
    public User mUser = new User();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mChangeName = (EditText) findViewById(R.id.nomedit);

        preferences = getSharedPreferences("firstname", MODE_PRIVATE);
        String firstname = preferences.getString("firstname", null);
        mUser.setFirstName(firstname);

        mChangeName.setText(firstname);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUser.setFirstName(mChangeName.getText().toString());
        preferences = getSharedPreferences("firstname", MODE_PRIVATE);
        preferences.edit().putString("firstname", mChangeName.getText().toString()).apply();
    }
}
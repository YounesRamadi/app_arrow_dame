package activities.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import controller.services.User;

public class SettingsActivity extends AppCompatActivity {

    public User mUser = new User();
    private EditText mChangeName;
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
                Uri myURI = Uri.parse("https://youtu.be/O-MQC_G9jTU?t=24");
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
package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import controller.User;

public class LoginActivity extends AppCompatActivity {

    private TextView mAskingName;
    private EditText mNameInput;
    private Button mValidateButton;
    public User mUser = new User();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAskingName = (TextView) findViewById(R.id.nomtext);
        mNameInput = (EditText) findViewById(R.id.nomedit);
        mValidateButton = (Button) findViewById(R.id.validate);

        mValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MenuActivity = new Intent(LoginActivity.this, MenuActivity.class);
                mUser.setFirstName(mNameInput.getText().toString());
                preferences = getSharedPreferences("firstname", MODE_PRIVATE);
                preferences.edit().putString("firstname", mNameInput.getText().toString()).apply();
                startActivity(MenuActivity);
            }
        });

    }
}
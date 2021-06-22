package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import controller.User;

public class LoginActivity extends AppCompatActivity {

    public User mUser = new User();
    private TextView mAskingName;
    private EditText mNameInput;
    private Button mValidateButton;

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
                MenuActivity.putExtra("nom", mNameInput.getText().toString());
                startActivity(MenuActivity);
                mUser.setFirstName(mNameInput.getText().toString());
            }
        });

    }
}
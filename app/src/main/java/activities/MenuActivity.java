package activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView mName;
    private Button mNewGame;
    private Button mTutoriel;
    private Button mOptions;
    private Button mCredits;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mName = (TextView) findViewById(R.id.bienvenue);
        mNewGame = (Button) findViewById(R.id.newgame);
        mTutoriel = (Button) findViewById(R.id.tutoriel);
        mOptions = (Button) findViewById(R.id.options);
        mCredits = (Button) findViewById(R.id.credits);

        mName.setText("Bienvenue " + getIntent().getStringExtra("nom") + " !");

        mNewGame.setTag(0);
        mTutoriel.setTag(1);
        mOptions.setTag(2);
        mCredits.setTag(3);

        mNewGame.setOnClickListener(this);
        mTutoriel.setOnClickListener(this);
        mOptions.setOnClickListener(this);
        mCredits.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int menuIndex = (int) v.getTag();

        switch (menuIndex) {
            case 0:
                Intent DisplayBoardActivity = new Intent(MenuActivity.this, DisplayBoardActivity.class);
                startActivity(DisplayBoardActivity);
                break;
            case 1:
                Intent TutorielActivity = new Intent(MenuActivity.this, TutorielActivity.class);
                startActivity(TutorielActivity);
                break;
            /*case 2:
                Intent SettingsActivity = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(SettingsActivity);
                break;
            case 3:
                Intent CreditsActivity = new Intent(MenuActivity.this, CreditsActivity.class);
                startActivity(CreditsActivity);
                break;*/
            default:
                // code block
        }
    }
}
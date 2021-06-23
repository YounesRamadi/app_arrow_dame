package activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

public class ChooseGameActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView mName;
    private Button mlocalGame;
    private Button mComputerGame;
    private Button mOnlineGame;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        mName = (TextView) findViewById(R.id.NouvellePartie);
        mlocalGame = (Button) findViewById(R.id.localGame);
        mComputerGame = (Button) findViewById(R.id.computerGame);
        mOnlineGame = (Button) findViewById(R.id.onlineGame);


        mlocalGame.setTag(0);
        mComputerGame.setTag(1);
        mOnlineGame.setTag(2);

        mOnlineGame.setOnClickListener(this);
        mlocalGame.setOnClickListener(this);
        mComputerGame.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int menuIndex = (int) v.getTag();

        switch (menuIndex) {
            case 0:
                Intent DisplayBoardActivity = new Intent(ChooseGameActivity.this, DisplayBoardActivity.class);
                startActivity(DisplayBoardActivity);
                break;
            case 1:
                Intent DisplayBoardIaActivity = new Intent(ChooseGameActivity.this, DisplayBoardIaActivity.class);
                startActivity(DisplayBoardIaActivity);
                break;
            case 2:
                Intent BluetoothActivity = new Intent(ChooseGameActivity.this, BluetoothActivity.class);
                startActivity(BluetoothActivity);;
                break;
            default:
                break;
        }
    }
}

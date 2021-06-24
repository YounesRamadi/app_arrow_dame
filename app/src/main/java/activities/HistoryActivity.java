package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apadnom.R;

import java.io.IOException;

import controller.History;

public class HistoryActivity extends AppCompatActivity {

    private Button test;
    private History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        try {
            history = new History(this, this.getFilesDir().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        test = (Button) findViewById(R.id.button2);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(history.read());
            }
        });

    }
}
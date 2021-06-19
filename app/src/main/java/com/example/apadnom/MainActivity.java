package com.example.apadnom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.apadnom.controller.GameBoard;

import controller.Pion;


public class MainActivity extends AppCompatActivity {

    private GameBoard game;

    private AbsoluteLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.myLayout = (AbsoluteLayout) findViewById(R.id.layout);

        game = new GameBoard();

        int y = 0;
        int x = 0;
        for(int i = 0; i < 7; i ++){
            y += 100;
            x = 50*(i%2);
            Pion[][] disp = game.display();
            for(int j = 0; j < 9; j ++) {
                if(disp[i][j] == null){
                    x += 100;
                    continue;
                }

                ImageView img = new ImageView(this);
                img.setImageDrawable(getDrawable(disp[i][j].getImg()));
                AbsoluteLayout.LayoutParams parms = new AbsoluteLayout.LayoutParams(100, 100, x, y);
                if(disp[i][j].get_direction() == 0)
                    img.setRotation(270);
                else
                    img.setRotation(90);
                img.setLayoutParams(parms);

                x += 100;

                myLayout.addView(img);
            }
        }

    }
}
package com.example.apadnom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apadnom.controller.GameBoard;
import com.example.apadnom.DisplayB;

import controller.Pion;


public class MainActivity extends AppCompatActivity {

    private GameBoard game;
    private DisplayB display;
    private AbsoluteLayout myLayout;
    private Pion[][] display_mat = new Pion[7][9];
    private long[] selected = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.myLayout = (AbsoluteLayout) findViewById(R.id.layout);

        game = new GameBoard();
        // display = new DisplayB(game, this);

        update();

    }

    public void setSelected(int a, int b) {
        this.selected[0] = a;
        this.selected[1] = b;
    }

    public long[] getSelected(){
        int ad = 0;
        long tmp = selected[1];
        if(selected[0] == 0){
            ad = 7;
            selected[1] = tmp + 4;
        }
        if(selected[0] == 1){
            selected[1] = Math.abs(selected[1] - 1) %9;
        }
        if(selected[0] == 2){
            selected[1] = Math.abs(selected[1] - 2) %9;
        }
        if(selected[0] == 5){
            selected[1] = (selected[1] + 1) % 9;
        }
        if(selected[0] == 6){
            selected[1] = (selected[1] + 1) % 9;
        }
        return selected;
    }

    public void update(){
        this.display_mat = game.display();
        int y = 0;
        int x = 0;
        for (int i = 0; i < 7; i++) {
            y += 100;
            x = 50 * (i % 2);
            for (int j = 0; j < 9; j++) {
                if (display_mat[i][j] == null) {
                    x += 100;
                    continue;
                }

                ImageView img = new ImageView(this);
                img.setImageDrawable(getDrawable(display_mat[i][j].getImg()));
                AbsoluteLayout.LayoutParams parms = new AbsoluteLayout.LayoutParams(100, 100, x, y);
                int finalI = i;
                int finalJ = j;
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelected(finalI, finalJ);
                        update();
                    }
                });

                if (display_mat[i][j].get_direction() == 0)
                    img.setRotation(270);
                else
                    img.setRotation(90);
                img.setLayoutParams(parms);

                x += 100;

                myLayout.addView(img);

                String s = getSelected()[0]  + ":" + getSelected()[1];
                TextView t = (TextView) findViewById(R.id.selected);
                t.setText(s);
            }
        }
    }
}
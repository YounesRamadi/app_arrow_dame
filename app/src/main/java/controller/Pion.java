package controller;

import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.apadnom.R;

public class Pion {
    protected byte color; // 1 noir 0 blanc
    protected byte direction; // 0 vers le nord / haut

    private int img = R.drawable.hexagone;

    public int getImg() {
        return img;
    }

    // 1 vers le sud / bas
    public Pion(){
        this.color = -1;
        this.direction = -1;
    }
    public Pion(byte pCouleur, byte pDirection){
        this.color = pCouleur;
        this.direction = pDirection;
    }

    public byte get_color(){
        return this.color;
    }

    public byte get_direction(){
        return this.direction;
    }

    public void set_direction(int pDirection){
        this.direction = direction;
    }

    public String toString(){
        if(this.color == 0){
            return "  ";
        }
        return "  ";
    }
}

package controller;

import androidx.annotation.DrawableRes;

import com.example.apadnom.R;


public class Pion {
    protected byte color; // 1 noir 0 blanc
    protected byte direction; // 0 vers le nord / haut

    private @DrawableRes
    int img = R.drawable.hexagone;

    // 1 vers le sud / bas
    public Pion() {

        this.color = -1;
        this.direction = -1;
    }

    public Pion(byte pCouleur, byte pDirection) {
        this.color = pCouleur;
        this.direction = pDirection;
    }
    public Pion copy(){
        Pion retour = new Pion();
        retour.color = color;
        retour.direction = direction;
        return retour;
    }
    public byte get_color(){
        return this.color;
    }

    public byte get_direction() {
        return this.direction;
    }

    public void set_direction(int pDirection) {
        this.direction = direction;
    }

    public void change_direction() {
        this.direction = (byte) Math.abs((int) direction - 1);
    }

    public String toString() {
        if (this.color == 0) {
            return "  ";
        }
        return "  ";
    }
}

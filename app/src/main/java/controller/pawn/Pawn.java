package controller.pawn;

import androidx.annotation.DrawableRes;

import com.example.apadnom.R;


public class Pawn {


    // 0 -> white
    // 1 -> black
    protected byte color;

    // 0 -> top
    // 1 -> bottom
    protected byte direction;

    private @DrawableRes
    int img = R.drawable.hexagone_green;


    /**
     * Default constructor
     */
    public Pawn() {
        this.color = -1;
        this.direction = -1;
    }

    /**
     * Constructor
     *
     * @param pColor     color of the pawn
     * @param pDirection direction of the pawn
     */
    public Pawn(byte pColor, byte pDirection) {
        setColor(pColor);
        setDirection(pDirection);
    }

    /**
     * Copy method
     *
     * @return a copy of the pawn
     */
    public Pawn copy() {
        Pawn retour = new Pawn();
        retour.color = color;
        retour.direction = direction;
        return retour;
    }

    public byte getColor() {
        return this.color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public @DrawableRes
    int getImg() {
        return img;
    }

    public void setImg(@DrawableRes int img) {
        this.img = img;
    }

    public byte getDirection() {
        return this.direction;
    }

    public void setDirection(byte direction) {
        this.direction = direction;
    }

    public void changeDirection() {
        this.direction = (byte) Math.abs((int) direction - 1);
    }

    public String toString() {
        return "  ";
    }
}

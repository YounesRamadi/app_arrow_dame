package controller.pawn;

import com.example.apadnom.R;

public class Star extends Pawn {

    public Star() {
        super();
    }

    public Star(byte pCouleur, byte direction) {

        super(pCouleur, direction);
        if (color == 0)
            super.setImg(R.drawable.blue_star);
        else
            super.setImg(R.drawable.red_star);
    }

    public Star copy() {
        Star retour = new Star();
        retour.color = color;
        retour.direction = direction;
        return retour;
    }

    public String toString() {
        if (this.color == 0) {
            return "Eb";
        }
        return "En";
    }
}

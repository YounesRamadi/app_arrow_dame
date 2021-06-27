package controller.pawn;

import com.example.apadnom.R;

public class Arrow extends Pawn {

    /**
     * Default constructor
     */
    public Arrow() {
        super();
    }

    public Arrow(byte pCouleur, byte direction) {
        super(pCouleur, direction);
        if (color == 0)
            super.setImg(R.drawable.blue_arrow);
        else
            super.setImg(R.drawable.red_arrow);
    }

    public Arrow copy(){
        Arrow retour = new Arrow();
        retour.color = color;
        retour.direction = direction;
        return retour;
    }

    public String toString(){
        if(this.color == 0){
            return "Fb";
        }
        return "Fn";
    }
}

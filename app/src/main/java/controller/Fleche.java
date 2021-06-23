package controller;

import com.example.apadnom.R;

public class Fleche extends Pion{
    public Fleche(byte pCouleur, byte direction){
        super(pCouleur, direction);
        if(color == 0)
            super.setImg(R.drawable.blue_arrow);
        else
            super.setImg(R.drawable.red_arrow);
    }

    public Fleche(){
        super();
    }

    public Fleche copy(){
        Fleche retour = new Fleche();
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

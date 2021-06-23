package controller;

import com.example.apadnom.R;

public class Etoile extends Pion{

    public Etoile(byte pCouleur, byte direction){

        super(pCouleur, direction);
        if(color == 0)
            super.setImg(R.drawable.blue_star);
        else
            super.setImg(R.drawable.red_star);
    }
    public Etoile copy(){
        Etoile retour = new Etoile();
        retour.color = color;
        retour.direction = direction;
        return retour;
    }
    public Etoile(){
        super();
    }

    public String toString(){
        if(this.color == 0){
            return "Eb";
        }
        return "En";
    }
}

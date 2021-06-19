package controller;

public class Fleche extends Pion{
    public Fleche(byte pCouleur, byte direction){
        super(pCouleur, direction);
    }
    public Fleche(){
        super();
    }
    public String toString(){
        if(this.couleur == 0){
            return "Fb";
        }
        return "Fn";
    }
}

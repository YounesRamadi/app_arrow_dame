package controller;

public class debug extends Pion{
    public debug(byte pCouleur, byte direction){
        super(pCouleur, direction);
    }
    public debug(){
        super();
    }
    public String toString(){
        if(this.couleur == 0){
            return "Ab";
        }
        return "An";
    }
}


package controller;

public class Etoile extends Pion{

    public Etoile(byte pCouleur, byte direction){

        super(pCouleur, direction);
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

package controller;

public class Pion {
    protected byte couleur; // 1 noir 0 blanc
    protected byte direction; // 0 vers le nord / haut
    // 1 vers le sud / bas
    public Pion(){
        this.couleur = -1;
        this.direction = -1;
    }
    public Pion(byte pCouleur, byte pDirection){
        this.couleur = pCouleur;
        this.direction = pDirection;
    }

    public byte get_couleur(){
        return this.couleur;
    }
    public byte get_direction(){
        return this.direction;
    }
    public void set_direction(int pDirection){
        this.direction = direction;
    }
    public String toString(){
        if(this.couleur == 0){
            return "  ";
        }
        return "  ";
    }
}

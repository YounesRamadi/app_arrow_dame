package controller;

public class TurnManager {
    protected int turn = 0; //0 pour blanc(bas) 1 pour noir(haut)
    protected int nb_white_star;
    protected int nb_black_star;

    public TurnManager(){

    }

    public void set_nb_white_star(int nb_white_star){
        this.nb_white_star = nb_white_star;
    }
    public void set_nb_black_star(int nb_black_star){
        this.nb_black_star = nb_black_star;
    }

    public void change_turn(){
        this.turn = turn++;
    }

    public void play(GameBoard gameboard){

    }

    public void newTurn(GameBoard gameboard){
        gameboard.setHas_jumped((byte)0);
    }

}

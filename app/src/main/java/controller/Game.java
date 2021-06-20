package controller;

public class Game {
    private GameBoard gameBoard;
    private int turn;
    private int jump;
    private int[][] possible_jump;
    private  int[][] possible_move;
    private byte has_jumped;
    private int[] selected = new int[2];

    public void Game(){
        turn = 0;
        jump = 0;
        has_jumped = (byte) 0;
        selected[0] = -1;
        selected[1] = -1;

        gameBoard = new GameBoard();
    }

}

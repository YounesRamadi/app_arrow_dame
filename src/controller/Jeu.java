package controller;
public interface Jeu {
    int[][] tableau=new int[7][9];
    public Pion[][] get_plateau();
    public int[][] get_possible_jump();
    public int[][] get_possible_move();
    public int[][] get_possibilities(Pion p, int posx, int posy);
    public Pion get_pion();
    public int move(int posx, int posy);
    public byte get_hasJumped();
    public int get_jump();
    public void set_hasJumped(byte hasJump);
}

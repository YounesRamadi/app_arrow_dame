package com.example.apadnom.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.example.apadnom.R;

import java.util.ArrayList;

import controller.Pion;
import controller.Etoile;
import controller.Fleche;

public class GameBoard {
    private Pion[][] gameboard = new Pion[7][9];
    private int nb_W_stars;
    private int nb_B_stars;

    private byte has_jumped;

    private int[][] possible_jump;
    private  int[][] possible_move;

    public Pion[][] getGameboard() {
        return gameboard;
    }

    public GameBoard() {
        initGameBoard();
        setNb_B_stars();
        setNb_W_stars();
    }

    public byte getHas_jumped(){
        return has_jumped;
    }

    public void setHas_jumped(byte j){
        has_jumped = j;
    }

    public int[][] getPossible_move() {
        return possible_move;
    }

    public int[][] getPossible_jump() {
        return possible_jump;
    }

    public void setPossible_jump(int[][] possible_jump) {
        this.possible_jump = possible_jump;
    }

    public void setPossible_move(int[][] possible_move) {
        this.possible_move = possible_move;
    }

    public void setNb_W_stars(int nb_W_stars) {
        this.nb_W_stars = nb_W_stars;
    }

    private void initGameBoard() {
        int[] tempgameboard = new int[63];

        for (int i = 0; i < 63; i++) {
            if (i < 3) {
                tempgameboard[i] = 2;
            } else if (i < 6) {
                tempgameboard[i] = 1;
            } else if (i < 9) {
                tempgameboard[i] = -1;
            } else if (i < 16) {
                tempgameboard[i] = 2;
            } else if (i < 18) {
                tempgameboard[i] = -1;
            } else if (i == 26) {
                tempgameboard[i] = -1;
            } else if (i == 36) {
                tempgameboard[i] = -1;
            } else if (i < 45) {
                tempgameboard[i] = 0;
            } else if (i < 47) {
                tempgameboard[i] = -1;
            } else if (i < 54) {
                tempgameboard[i] = 4;
            } else if (i < 57) {
                tempgameboard[i] = -1;
            } else if (i < 60) {
                tempgameboard[i] = 3;
            } else if (i < 63){
                tempgameboard[i] = 4;
            } else{
                tempgameboard[i] = -1;
            }
        }

        int a = 0;
        int j = 0;
        for (int i = 0; i < 63; i++, a++) {
            if (a % 9 == 0) {
                a = 0;
            }
            switch (tempgameboard[i]) {
                case -1:
                    gameboard[j][a] = null;
                    break;

                case 1:
                    gameboard[j][a] = new Etoile((byte)1,(byte)1);
                    break;
                case 2:
                    gameboard[j][a] = new Fleche((byte)1,(byte)1);
                    break;
                case 3:
                    gameboard[j][a] = new Etoile((byte)0,(byte)0);
                    break;
                case 4:
                    gameboard[j][a] = new Fleche((byte)0,(byte)0);
                    break;
                default :
                    gameboard[j][a] = new Pion();
                    break;
            }
            if ((i + 1) % 9 == 0) {
                j++;

            }
        }
    }

    public void setCell(int x, int y, Pion p) {
        if(p != null)
            gameboard[x][y] = p;
    }

    public int getNb_W_stars() {
        return nb_W_stars;
    }

    public void setNb_W_stars() {
        int n = 0;
        Pion p;

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                p = gameboard[i][j];
                if ((p instanceof Etoile) && (p.get_color() == 0))
                    n++;
            }
        }
    }

    public int getNb_B_stars() {
        return nb_B_stars;
    }

    public void setNb_B_stars() {
        int n = 0;
        Pion p;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                p = gameboard[i][j];
                if ((p instanceof Etoile) && (p.get_color() == 1))
                    n++;
            }
        }
        nb_B_stars = n;
    }

    public Pion getCell(int x, int y) {
        return this.gameboard[x][y];
    }

    // check_selection permet de verifier que l'utilisateur puisse prendre la piece
    // honetement on sait pas ce qu'elle fait
    public int check_selection(int x, int y, int turn) {

        if (gameboard[x][y] == null || gameboard[x][y].get_color() == -1) {
            return -1;
        }
        int actual_color = gameboard[x][y].get_color();
        //on commence par les blancs
        // tour % 2 == 0 -> blanc
        if (actual_color != turn % 2) {
            return -1;
        }
        get_possibilities(gameboard[x][y], x, y);
        if (possible_jump != null) {
            return 0;
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                if (gameboard[i][j].get_color() == actual_color && !(i != x || j != y)) {
                    get_possibilities(gameboard[i][j], i, j);
                    if (possible_jump != null) {
                        possible_jump = null;
                        return -1;
                    }
                }
            }
        }
        return 0;
    }


    public int[][] get_possibilities(Pion p, int x, int y) {
        if (gameboard[x][y] instanceof Etoile) {
            return get_possibilitiesStar(gameboard[x][y], x, y);
        }
        return get_possibilitiesArrow(gameboard[x][y], x, y);
    }


    private int[][] get_possibilitiesStar(Pion p, int x, int y) {
        ArrayList<int[]> arl = new ArrayList<int[]>();
        ArrayList<int[]> ar2 = new ArrayList<int[]>();

        int[] liste_int = new int[2];
        int[] tmp = new int[2];

        int indexMove = 0;
        int indexJump = 0;

        if (p.get_direction() == 0) {
            //jumps
            if ((x - 2) >= 0 && gameboard[x - 1][y].get_color() != -1 && gameboard[x - 2][y].get_color() == -1) { // pion P() -> 0
                tmp[0] = x - 2;
                tmp[1] = y;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                // gameboard[x-2][y] = new debug();
            }
            if ((x + 2) < 7 && gameboard[x][y + 2].get_color() == -1 && gameboard[x][y + 1].get_color() != -1) {
                tmp[0] = x;
                tmp[1] = y + 2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                // gameboard[x][y+2] = new debug();
            }
            if ((y - 2) >= 0 && gameboard[x][y - 2].get_color() == -1 && gameboard[x][y - 1].get_color() != -1) {
                tmp[0] = x;
                tmp[1] = y - 2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                // gameboard[x][y - 2] = new debug();
            }
            if (x - 2 >= 0 && y - 2 >= 0 && gameboard[x - 2][y - 2].get_color() == -1 && gameboard[x - 1][y - 1].get_color() != -1) {
                tmp[0] = x - 2;
                tmp[1] = y - 2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                //gameboard[x - 2][y - 2] = new debug();
            }
            //move
            if (indexJump == 0) {
                if (x - 1 >= 0 && gameboard[x - 1][y].get_color() == -1) { // pion P() -> 0
                    tmp[0] = x - 1;
                    tmp[1] = y;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x - 1][y] = new debug();
                }
                if (y + 1 < 9 && gameboard[x][y + 1].get_color() == -1) {
                    tmp[0] = x;
                    tmp[1] = y + 1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x][y + 1] = new debug();
                }
                if (y - 1 >= 0 && gameboard[x][y - 1].get_color() == -1) {
                    tmp[0] = x;
                    tmp[1] = y - 1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x][y - 1] = new debug();
                }
                if (y - 1 >= 0 && x - 1 >= 0 && gameboard[x - 1][y - 1].get_color() == -1) {
                    tmp[0] = x - 1;
                    tmp[1] = y - 1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x - 1][y - 1] = new debug();
                }
            }
        } else {
            //jumps
            if (x + 2 < 7 && gameboard[x + 2][y].get_color() == -1 && gameboard[x + 1][y].get_color() != -1) { // pion P() -> 0
                tmp[0] = x + 2;
                tmp[1] = y;
                arl.add(indexMove++, tmp);
                //debugage a enlever vitezef
                //gameboard[x + 2][y] = new debug();
            }
            if (y - 2 >= 0 && gameboard[x][y - 2].get_color() == -1 && gameboard[x][y - 1].get_color() != -1) {
                tmp[0] = x;
                tmp[1] = y - 2;
                arl.add(indexMove++, tmp);
                //debugage a enlever vitezef
                //gameboard[x][y - 2] = new debug();
            }
            if (y + 2 < 9 && gameboard[x][y + 2].get_color() == -1 && gameboard[x][y + 1].get_color() != -1) {
                tmp[0] = x;
                tmp[1] = y + 2;
                arl.add(indexMove++, tmp);
                //debugage a enlever vitezef
                //gameboard[x][y + 2] = new debug();
            }
            if (y + 2 < 9 && x + 2 < 7 && gameboard[x + 2][y + 2].get_color() == -1 && gameboard[x + 1][y + 1].get_color() != -1) {
                tmp[0] = x + 2;
                tmp[1] = y + 2;
                arl.add(indexMove++, tmp);
                //debugage a enlever vitezef
                //gameboard[x + 2][y + 2] = new debug();
            }
            //move
            if (indexJump == 0) {
                if (x + 1 < 7 && gameboard[x + 1][y].get_color() == -1) { // pion P() -> 0
                    tmp[0] = x + 1;
                    tmp[1] = y;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x + 1][y] = new debug();
                }
                if (y - 1 >= 0 && gameboard[x][y - 1].get_color() == -1) {
                    tmp[0] = x;
                    tmp[1] = y - 1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x][y - 1] = new debug();
                }
                if (y + 1 < 9 && gameboard[x][y + 1].get_color() == -1) {
                    tmp[0] = x;
                    tmp[1] = y + 1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x][y + 1] = new debug();
                }
                if (x + 1 < 7 && y + 1 < 9 && gameboard[x + 1][y + 1].get_color() == -1) {
                    tmp[0] = x + 1;
                    tmp[1] = y + 1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x + 1][y + 1] = new debug();
                }
            }
        }
        setPossible_move(arl.toArray(new int[0][0]));
        setPossible_jump(ar2.toArray(new int[0][0]));
        int[][] moves = getPossible_move();
        int[][] jumps = getPossible_jump();
        int[][] possiblities = new int[indexJump + indexMove][2];
        for (int i = 0; i < indexJump + indexMove; i++) {
            if (i < indexMove && indexMove != 0) {
                possiblities[i] = moves[i];
            }
            if (i >= indexMove && indexJump != 0) {
                possiblities[i] = jumps[i % indexMove];
            }
        }
        return possiblities;
    }


    private int[][] get_possibilitiesArrow(Pion p, int x, int y){
        ArrayList<int[]> arl=new ArrayList<int[]>(); // moves
        ArrayList<int[]> ar2=new ArrayList<int[]>(); //jump

        int[] liste_int = new int[2];
        int[] tmp = new int[2];

        int indexMove = 0;
        int indexJump = 0;
        if (p.get_direction() == 0){
            //jumps
            if(x -2 >= 0 && gameboard[x-1][y].get_color() != -1 && gameboard[x-2][y].get_color() == -1){ // pion P() -> 0
                tmp[0] = x-2;
                tmp[1] = y;
                ar2.add(indexJump++,  tmp);
                //debugage a enlever vitezef
                //gameboard[x-2][y] = new debug();
            }
            if(y +2 < 9 && gameboard[x][y+2].get_color() == -1 && gameboard[x][y+1].get_color() != -1 && has_jumped == (byte)1){
                tmp[0] = x;
                tmp[1] = y+2;
                ar2.add(indexJump++, tmp);
                //debugage a enlever vitezef
                //gameboard[x][y+2] = new debug();
            }
            if(y-2 >= 0 && gameboard[x][y-2].get_color() == -1 && gameboard[x][y-1].get_color() != -1 && has_jumped == (byte)1){
                tmp[0] = x;
                tmp[1] = y-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                //gameboard[x][y-2] = new debug();
            }
            if(x-2 >= 0 && y-2 >= 0 && gameboard[x-2][y-2].get_color() == -1 && gameboard[x-1][y-1].get_color() != -1){
                tmp[0] = x-2;
                tmp[1] = y-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                //gameboard[x-2][y-2] = new debug();
            }
            if(indexJump == 0){
                //move
                if(x-1 >= 0 &&gameboard[x-1][y].get_color() == -1){ // pion P() -> 0
                    tmp[0] = x-1;
                    tmp[1] = y;
                    arl.add(indexMove++,  tmp);
                    //debugage a enlever vitezef
                    //gameboard[x-1][y] = new debug();
                }
                if( x-1 >= 0 &&y-1 >= 0 &&gameboard[x-1][y-1].get_color() == -1){
                    tmp[0] = x-1;
                    tmp[1] = y-1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x-1][y-1] = new debug();
                }
            }
        }
        else {
            //jumps
            if(x+2 < 7 && gameboard[x+2][y].get_color() == -1 && gameboard[x+1][y].get_color() != -1){ // pion P() -> 0
                tmp[0] = x+2;
                tmp[1] = y;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                //gameboard[x+2][y] = new debug();
            }
            if(y-2 >= 0 && gameboard[x][y-2].get_color() == -1 && gameboard[x][y-1].get_color() != -1 && has_jumped == (byte)1){
                tmp[0] = x;
                tmp[1] = y-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                //gameboard[x][y-2] = new debug();
            }
            if(y+2 < 9 && gameboard[x][y+2].get_color() == -1 && gameboard[x][y+1].get_color() != -1 && has_jumped == (byte)1){
                tmp[0] = x;
                tmp[1] = y+2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                //gameboard[x][y+2] = new debug();
            }
            if(y+2 < 9 && x+2 < 7 && gameboard[x+2][y+2].get_color() == -1 && gameboard[x+1][y+1].get_color() != -1){
                tmp[0] = x+2;
                tmp[1] = y+2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                //gameboard[x+2][y+2] = new debug();
            }
            //move
            if(indexJump == 0){
                if(x+1 < 7 && gameboard[x+1][y].get_color() == -1){ // pion P() -> 0
                    tmp[0] = x+1;
                    tmp[1] = y;
                    arl.add(indexMove++,  tmp);
                    //debugage a enlever vitezef
                    //gameboard[x+1][y] = new debug();
                }
                if(x+1 < 7 && y+1 < 9 && gameboard[x+1][y+1].get_color() == -1){
                    tmp[0] = x+1;
                    tmp[1] = y+1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    //gameboard[x+1][y+1] = new debug();
                }
            }
        }

        setPossible_move(arl.toArray(new int[0][0]));
        setPossible_jump(ar2.toArray(new int[0][0]));
        int[][] moves = getPossible_move();
        int[][] jumps = getPossible_jump();

        int[][] possibilities = new int[indexJump+indexMove][2];
        for(int i=0; i<indexJump+indexMove;i++){
            if(i<indexMove && indexMove != 0){
                possibilities[i] = moves[i];
            }
            if(i>=indexMove && indexJump !=0){
                possibilities[i] = jumps[i-indexMove];
            }
        }
        return possibilities;
    }

    public Pion[][] display(){
        Pion[][] display = new Pion[7][9];

        for(int i = 0; i < 9; i++){
            display[0][i] = gameboard[0][(i+7)%9];
            display[1][i] = gameboard[1][(i+8)%9];
            display[2][i] = gameboard[2][(i+8)%9];

            display[3][i] = gameboard[3][i];

            display[4][i] = gameboard[4][i];
            display[5][i] = gameboard[5][(i+1)%9];
            display[6][i] = gameboard[6][(i+1)%9];

        }

        return display;
    }
}

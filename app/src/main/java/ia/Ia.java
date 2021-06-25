package ia;

import controller.Etoile;
import controller.Fleche;
import controller.GameBoard;
import controller.Pion;
import java.util.Random;



public class Ia {
    int jumpB=0;
    int jumpW=0;

    /**
     * calculate the score for the current game state for the IA
     * @param j the game state at the end of the algorithm
     * @param color the color of the last pawn wich has moved
     * @return the score of the current game state for the IA
     */
    public int valuation(GameBoard j,byte color) {
        int fEtoile=3;
        int score=5000;
        if(j.getNb_B_stars()==0)
            return 10000000;
        if(j.getNb_B_stars()==2)
            score+= 20000;
        if(j.getNb_B_stars()==1)
            score+= 30000;
        if(j.getNb_B_stars()==0)
            return 0;
        if(color==1)
            score+=jumpB*90;
        if(color==0) {
            if (jumpW >= 1)
                score-=jumpW*90;
            if (jumpW >= 4)
                return 0;
        }
        jumpW=0;
        jumpB=0;
        //score+= j.getJump()*50000000;
        //System.out.println("A : "+j.getJump() + " B : " + j.getHas_jumped());

        for (int lig = 0; lig < 7; lig++) {
            for (int col = 0; col < 9; col++){
                if (j.getGameboard()[lig][col] != null){
                    j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                    if(j.getGameboard()[lig][col].get_color()==1){
                        score+=j.getPossible_move().length*2;
                        if(lig>0 && col>0 && lig<6 && col<8) {
                            if(j.getGameboard()[lig - 1][col - 1]!=null && j.getGameboard()[lig - 1][col]!=null && j.getGameboard()[lig + 1][col]!=null && j.getGameboard()[lig + 1][col + 1]!=null && j.getGameboard()[lig + 1][col-1]!=null) {
                                if (j.getGameboard()[lig - 1][col - 1].get_color() == 1 && j.getGameboard()[lig - 1][col].get_color() == 1) {
                                    score += 20;
                                }
                                if (j.getGameboard()[lig + 1][col].get_color() == 1 && j.getGameboard()[lig + 1][col + 1].get_color() == 1) {
                                    score += 20;
                                }
                                if (j.getGameboard()[lig + 1][col].get_color() != -1 && j.getGameboard()[lig-1][col].get_color() != -1) {
                                    score += 30;
                                }
                                if (j.getGameboard()[lig + 1][col-1].get_color() != -1 && j.getGameboard()[lig-1][col-1].get_color() != -1) {
                                    score += 30;
                                }
                            }
                        }
                        if(color==0) {
                            score+=j.getPossible_jump().length*6;
                        }
                    }
                    else{
                        score-=j.getPossible_jump().length*4;
                        score-=j.getPossible_move().length*3;
                        if(color==1)
                            score-=j.getPossible_jump().length*2;
                    }
                    //color 1 ceux qui sont haut
                    //color 0 ceux qui sont en bas
                    if(j.getGameboard()[lig][col].get_direction()!=1 && j.getGameboard()[lig][col].get_color()==1)
                        score+=10;
                    if(lig+2<=6 && col+2<9)
                        if(j.getGameboard()[lig+1][col]!=null && j.getGameboard()[lig+2][col]!=null && j.getGameboard()[lig+1][col+1]!=null && j.getGameboard()[lig+2][col+2]!=null) {
                            if (j.getGameboard()[lig][col].get_color() == 1 && j.getGameboard()[lig][col] instanceof Etoile) {
                                score += lig * 85;
                                if ((j.getGameboard()[lig + 1][col].get_color() != -1 && j.getGameboard()[lig + 2][col].get_color() == -1) || (j.getGameboard()[lig + 1][col + 1].get_color() != -1 && j.getGameboard()[lig + 2][col + 2].get_color() == -1))
                                    score += 20;
                            }
                        }
                    if(j.getGameboard()[lig][col].get_color()==0 && j.getGameboard()[lig][col] instanceof Etoile){
                        score-=(6-lig)*84;
                    }
                }
            }
        }
        if(score <= 0){
            score = 1;
        }
        return score;
    }

    /**
     * keep the best choice for the IA
     * @param j current gamestate
     * @param ligne row of the last pawn moved
     * @param colonne colomn of the last pawn moved
     * @param lim   the limite in depth of the algorithm
     * @param sameColor true if the pawn moved before is the same color
     * @param lim2 the second limit for the depth of the jump
     * @return the best choice for the IA
     */
    public int max(GameBoard j, int ligne, int colonne, int lim, boolean sameColor, int lim2){
        if(!sameColor)
            j.setHas_jumped((byte) 0);
        int lemove=j.move(ligne, colonne);
        int max=0;
        int inter=0;
        int obl=0;
        int[][] poss;
        System.out.println("max jump color: " + ligne + "le move : " + colonne+ "le flag : "+ j.getJump() + "selected" +j.getSelection()[0]+"//"+j.getSelection()[1]+" Daronne de pierre ? "+ (j.getGameboard()[ligne][colonne] instanceof Etoile)+"  " +(j.getGameboard()[ligne][colonne].get_color()));
        if(j.getHas_jumped()==(byte)1 && sameColor && lim2>0){
            //System.out.println("Zebi:"+j.getJump());
            poss=j.get_possibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=max(j.copy(),poss[i][0],poss[i][1],lim, true, lim2-1);
                    if(max<inter){
                        max=inter;
                    }
                    inter=min(j.copy(),poss[i][0],poss[i][1],lim-1, false, lim2-1);
                    if(max<inter){
                        max=inter;
                    }
                }
            }
        }
        if(j.getJump()>1 && sameColor && lim2>0){
            jumpB=j.getJump();
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2-1);
                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(j.getJump()==1 && sameColor && lim2>0){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim-1, true, lim2-1);

                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(j.getJump()<=0){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {

                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim - 1, false, lim2-1);
                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim<=0 || lim2<=0 || j.getNb_W_stars()==0 || j.getNb_B_stars()==0){
            //System.out.println("Ceci est un test");
            if(sameColor)
                return valuation(j, (byte)(1));
            else
                return valuation(j, (byte)(0));
        }
        if(j.getJump()==0 && !sameColor){
            j.setHas_jumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() != j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col].get_color() != -1) {
                            j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if (j.getPossible_jump() != null) {
                                for (int n = 0; n < j.getPossible_jump().length; n++) {
                                    if ((j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != j.getGameboard()[lig][col].get_color()) {
                                        if (obl == 0)
                                            max = 0;
                                        inter = max(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true, lim2-1);
                                        if (max < inter)
                                            max = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        inter = max(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true, lim2-1);
                                        if (max < inter)
                                            max = inter;
                                        inter = min(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim -1, false, lim2-1);
                                        if (max < inter)
                                            max = inter;
                                    }
                                }
                            }
                        }

                        if (j.getPossible_move() != null && obl == 0) {
                            for (int n = 0; n < j.getPossible_move().length; n++) {
                                inter = min(j.copy(), j.getPossible_move()[n][0], j.getPossible_move()[n][0], lim - 1, false, lim2-1);
                                if (max < inter)
                                    max = inter;
                            }
                        }
                    }
                }
            }
        }
        //System.out.println("max : " + max + " - lim : " + lim);

        return max;
    }
    /**
     * keep the badest choice for the IA
     * @param j current gamestate
     * @param ligne row of the last pawn moved
     * @param colonne colomn of the last pawn moved
     * @param lim   the limite in depth of the algorithm
     * @param sameColor true if the pawn moved before is the same color
     * @param lim2 the second limit for the depth of the jump
     * @return the badest choice for the IA
     */
    public int min(GameBoard j, int ligne, int colonne, int lim, boolean sameColor, int lim2) {
        if(!sameColor)
            j.setHas_jumped((byte) 0);
        int lemove=j.move(ligne, colonne);
        System.out.println("lemove : "+ j.getJump()+"\n");
        int min=50000;
        int inter=0;
        int obl=0;
        int[][] poss;
        //System.out.println("max jump color: " + ligne + "le move : " + colonne+ "le flag : "+ j.getJump() + "selected" +j.getSelection()[0]+"//"+j.getSelection()[1]+" Daronne de pierre ? "+ (j.getGameboard()[ligne][colonne] instanceof Fleche)+"  ");
        if(j.getHas_jumped()==(byte)1 && sameColor && lim2>0){
            //System.out.println("Zebi:"+j.getJump());

            poss=j.get_possibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=min(j.copy(),poss[i][0],poss[i][1],lim, true, lim2-1);
                    if(min>inter){
                        min=inter;
                    }
                    inter=max(j.copy(),poss[i][0],poss[i][1],lim-1, false, lim2-1);
                    if(min>inter){
                        min=inter;
                    }
                }
            }
        }
        if(j.getJump()>1 && sameColor && lim2>0){
            jumpW=j.getJump();
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim, true, lim2-1);
                                    if (min > inter)
                                        min = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(j.getJump()==1 && sameColor && lim2>0){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim - 1, false, lim2-1);
                                    if (min > inter)
                                        min = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim<=0 || lim2<=0 || j.getNb_W_stars()==0 || j.getNb_B_stars()==0){
            if(sameColor)
                return valuation(j, (byte)(0));
            else
                return valuation(j, (byte)(1));
        }
        if(j.getJump()<=0 && !sameColor){
            j.setHas_jumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() != j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col].get_color() !=-1) {
                            j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if (j.getPossible_jump() != null) {
                                for (int n = 0; n < j.getPossible_jump().length; n++) {
                                    if ((j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != j.getGameboard()[lig][col].get_color() && (j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != -1) {
                                        if (obl == 0)
                                            min = 50000;
                                        inter = min(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true, lim2-1);
                                        if (min > inter)
                                            min = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        //System.out.println("la ligne "+ lig +" la colonne "+ col+" [0]: "+j.getPossible_jump()[n][0] +" ||  et [1]"+ j.getPossible_jump()[n][1]);
                                        inter = min(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true, lim2-1);
                                        if (min > inter)
                                            min = inter;
                                        inter = max(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim-1, false, lim2-1);
                                        if (min > inter)
                                            min = inter;
                                    }

                                }
                            }
                        }
                        //System.out.println("Getpossiblemove : " + j.getPossible_move()[0] + " / " + j.getPossible_move()[1]);
                        if (j.getPossible_move() != null && obl == 0) {
                            //System.out.println("le nombre de move possible d'après pierre :" + j.getPossible_move().length+"\n");
                            for (int n = 0; n < j.getPossible_move().length; n++) {
                                //System.out.println(inter+"\n");
                                inter = max(j.copy(), j.getPossible_move()[n][0], j.getPossible_move()[n][0], lim - 1, false, lim2-1);
                                if (min > inter)
                                    min = inter;
                            }
                        }

                    }

                }
            }
        }

        return min;
    }

    /**
     * calcul the best score against the best player
     * @param couleur color of the Ia in game
     * @param j current state of the game
     * @param lim limit of depth
     * @return  the best score against the best player
     */
    public int[] minMax(byte couleur, GameBoard j, int lim) {
        //System.out.println("fonction : " + j.getJump()+"\n");
        int lim2= 6;
        Pion[][] plateau = j.getGameboard();
        int[] pos= {-1,-1,-1,-1,0,0};
        int[][] possible_jump = {{-1,-1}};
        int[][] possible_move = {{-1,-1}};
        int[][] poss;
        Random rand= new Random();
        int obl=0;
        int max=0;
        int inter=0;
        if(j.getJump()>1){
            if(j.getHas_jumped()==(byte)1){
                poss=j.get_possibilities(j.getGameboard()[j.getMovedPawn()[0]][j.getMovedPawn()[1]], j.getMovedPawn()[0], j.getMovedPawn()[1]);
                if(poss!=null){
                    for(int i=0; i<poss.length; i++){
                        inter=max(j.copy(),poss[i][0],poss[i][1],lim, true, lim2-1);
                        if (max < inter) {
                            max = inter;
                            pos[0] = j.getMovedPawn()[0];
                            pos[1] = j.getMovedPawn()[1];
                            pos[2] = poss[i][0];
                            pos[3] = poss[i][1];
                            possible_move = j.getPossible_move();
                            possible_jump = j.getPossible_jump();
                        }
                    }
                }
            }
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == couleur && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2-1);
                                    if (max < inter) {
                                        max = inter;
                                        pos[0] = lig;
                                        pos[1] = col;
                                        pos[2] = poss[i][0];
                                        pos[3] = poss[i][1];
                                        possible_move = j.getPossible_move();
                                        possible_jump = j.getPossible_jump();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(j.getJump()==1){
            if(j.getHas_jumped()==(byte)1){
                poss=j.get_possibilities(j.getGameboard()[j.getMovedPawn()[0]][j.getMovedPawn()[1]], j.getMovedPawn()[0], j.getMovedPawn()[1]);
                if(poss!=null){
                    for(int i=0; i<poss.length; i++){
                        inter=max(j.copy(),poss[i][0],poss[i][1],lim, true, lim2-1);
                        if (max < inter) {
                            max = inter;
                            pos[0] = j.getMovedPawn()[0];
                            pos[1] = j.getMovedPawn()[1];
                            pos[2] = poss[i][0];
                            pos[3] = poss[i][1];
                            possible_move = j.getPossible_move();
                            possible_jump = j.getPossible_jump();
                        }
                    }
                }
            }
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == couleur && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim-1, false, lim2-1);
                                    if (max < inter) {
                                        max = inter;
                                        pos[0] = lig;
                                        pos[1] = col;
                                        pos[2] = poss[i][0];
                                        pos[3] = poss[i][1];
                                        possible_move = j.getPossible_move();
                                        possible_jump = j.getPossible_jump();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(j.getHas_jumped()==(byte)1){
            poss=j.get_possibilities(j.getGameboard()[j.getMovedPawn()[0]][j.getMovedPawn()[1]], j.getMovedPawn()[0], j.getMovedPawn()[1]);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=max(j.copy(),poss[i][0],poss[i][1],lim, true, lim2-1);
                    if (max < inter) {
                        max = inter;
                        pos[0] = j.getMovedPawn()[0];
                        pos[1] = j.getMovedPawn()[1];
                        pos[2] = poss[i][0];
                        pos[3] = poss[i][1];
                        possible_move = j.getPossible_move();
                        possible_jump = j.getPossible_jump();
                    }
                }
            }

        }
        else {
            for (int ligne = 0; ligne < 7; ligne++) {
                for (int col = 0; col < 9; col++) {
                    if (plateau[ligne][col] != null && !(plateau[ligne][col] instanceof Etoile)) {
                        if (plateau[ligne][col].get_color() == couleur) {
                            j.get_possibilities(plateau[ligne][col], ligne, col);
                            if (j.getPossible_move() != null) {
                                for (int s = 0; s < j.getPossible_move().length; s++) {
                                    inter = min(j.copy(), j.getPossible_move()[s][0], j.getPossible_move()[s][1], lim - 1, false, lim2-1);
                                    inter+= rand.nextInt(10);
                                    if (max < inter) {
                                        max = inter;
                                        pos[0] = ligne;
                                        pos[1] = col;
                                        pos[2] = j.getPossible_move()[s][0];
                                        pos[3] = j.getPossible_move()[s][1];
                                        possible_jump = j.getPossible_jump();
                                        possible_move = j.getPossible_move();
                                    }
                                }
                                if (j.getPossible_jump() != null) {
                                    for (int s = 0; s < j.getPossible_jump().length; s++) {
                                        if ((j.getGameboard()[ligne + (j.getPossible_jump()[s][0] - ligne) / 2][col + (j.getPossible_jump()[s][1] - col) / 2].get_color()) != j.getGameboard()[ligne][col].get_color()) {
                                            if (obl == 0)
                                                max = 0;
                                            inter = max(j.copy(), j.getPossible_jump()[s][0], j.getPossible_jump()[s][1], lim, true, lim2-1);
                                            inter+= rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossible_jump()[s][0];
                                                pos[3] = j.getPossible_jump()[s][1];
                                                possible_jump = j.getPossible_jump();
                                                possible_move = j.getPossible_move();
                                            }
                                            inter = min(j.copy(), j.getPossible_jump()[s][0], j.getPossible_jump()[s][1], lim-1, false, lim2-1);
                                            inter+= rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossible_jump()[s][0];
                                                pos[3] = j.getPossible_jump()[s][1];
                                                possible_jump = j.getPossible_jump();
                                                possible_move = j.getPossible_move();
                                            }
                                            obl = 1;
                                        } else if (obl == 0) {
                                            inter = max(j.copy(), j.getPossible_jump()[s][0], j.getPossible_jump()[s][1], lim, true, lim2-1);
                                            inter+= rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossible_jump()[s][0];
                                                pos[3] = j.getPossible_jump()[s][1];
                                                possible_jump = j.getPossible_jump();
                                                possible_move = j.getPossible_move();
                                            }
                                            inter = min(j.copy(), j.getPossible_jump()[s][0], j.getPossible_jump()[s][1], lim -1, false, lim2-1);
                                            inter+= rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossible_jump()[s][0];
                                                pos[3] = j.getPossible_jump()[s][1];
                                                possible_jump = j.getPossible_jump();
                                                possible_move = j.getPossible_move();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        j.setPossible_move(possible_move);
        j.setPossible_jump(possible_jump);
        j.setSelection(pos[0],pos[1]);

        System.out.println("Mverenvoi : " +j.move(pos[2],pos[3]) + " pos[0]/ pos1 :" + pos[0] + pos[1]+" va en "+pos[2]+pos[3]);
        pos[4]=j.getHas_jumped();
        pos[5]=j.getJump();
        return pos;
    }
}
package ia;

import controller.Etoile;
import controller.GameBoard;
import controller.Pion;


public class Ia {
    public int valuation(GameBoard j,byte color) {

        int fEtoile=3;
        int score=0;
        if(j.getNb_B_stars()==0 && color==1)
            return 500;
        else if(j.getNb_B_stars()==0 && color==0)
            return 0;
        else if(j.getNb_W_stars()==0 && color==0)
            return 500;
        else if(j.getNb_W_stars()==0 && color==1)
            return 0;
        //score+= j.getJump()*50000000;
        System.out.println("A : "+j.getJump() + " B : " + j.getHas_jumped());

        for (int lig = 0; lig < 7; lig++) {
            for (int col = 0; col < 9; col++){
                if (j.getGameboard()[lig][col] != null){
                    j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                    if(j.getGameboard()[lig][col].get_color()==color){
                        score+=j.getPossible_jump().length*2 +lig;
                        score+=j.getPossible_move().length;
                    }/*
                    else{
                        score-=j.getPossible_jump().length*2;
                        score-=j.getPossible_move().length;
                    }*/
                    //color 1 ceux qui sont haut
                    //color 0 ceux qui sont en bas
                    if(j.getGameboard()[lig][col].get_color()!=j.getGameboard()[lig][col].get_direction() && j.getGameboard()[lig][col].get_color()==color)
                        score+=20;
                    else if(j.getGameboard()[lig][col].get_color()==color && j.getGameboard()[lig][col] instanceof Etoile) {
                        score += lig*50;
                    }
                }
            }
        }
        if(score <= 0){
            score = 1;
        }
        return score;
    }
    public int max(GameBoard j, int ligne, int colonne, int lim, boolean sameColor){
        if(j.getPossible_jump()!=null && j.getPossible_jump().length!=0){
            System.out.println("les jumps : "+j.getPossible_jump().length);
        }
        int lemove=j.move(ligne, colonne);
        int max=0;
        int inter=0;
        int obl=0;
        int[][] poss;
        System.out.println("max jump color: " + j.getHas_jumped() + "le move : " + lemove);
        if(j.getHas_jumped()==(byte)1 && sameColor){
            System.out.println("Zebi:"+j.getJump());
            poss=j.get_possibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=max(j.copy(),poss[i][0],poss[i][1],lim, true);
                    if(max<inter){
                        max=inter;
                    }
                    inter=min(j.copy(),poss[i][0],poss[i][1],lim-1, false);
                    if(max<inter){
                        max=inter;
                    }
                }
            }
        }
        if(j.getJump()>1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true);
                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(j.getJump()==1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim-1, true);

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
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim - 1, false);
                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim<=0 || j.getNb_W_stars()==0 || j.getNb_B_stars()==0){
            System.out.println("Ceci est un test");
            return valuation(j, (byte)((j.getGameboard()[ligne][colonne].get_color()+1)%2));
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
                                        inter = max(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true);
                                        if (max < inter)
                                            max = inter;
                                        inter = min(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim-1, false);
                                        if (max < inter)
                                            max = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        inter = max(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true);
                                        if (max < inter)
                                            max = inter;
                                        inter = min(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim -1, false);
                                        if (max < inter)
                                            max = inter;
                                    }
                                }
                            }
                        }

                        if (j.getPossible_move() != null && obl == 0) {
                            for (int n = 0; n < j.getPossible_move().length; n++) {
                                inter = min(j.copy(), j.getPossible_move()[n][0], j.getPossible_move()[n][0], lim - 1, false);
                                if (max < inter)
                                    max = inter;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("max : " + max + " - lim : " + lim);

        return max;
    }

    public int min(GameBoard j, int ligne, int colonne, int lim, boolean sameColor) {
        j.move(ligne, colonne);
        int min=5000;
        int inter=0;
        int obl=0;
        int[][] poss;
        if(j.getHas_jumped()==(byte)1 && sameColor){
            System.out.println("Zebi:"+j.getJump());

            poss=j.get_possibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=min(j.copy(),poss[i][0],poss[i][1],lim, true);
                    if(min>inter){
                        min=inter;
                    }
                    inter=max(j.copy(),poss[i][0],poss[i][1],lim-1, false);
                    if(min>inter){
                        min=inter;
                    }
                }
            }
        }
        if(j.getJump()>1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim, true);
                                    if (min > inter)
                                        min = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(j.getJump()==1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim - 1, false);
                                    if (min > inter)
                                        min = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim<=0 || j.getNb_W_stars()==0 || j.getNb_B_stars()==0){
            System.out.println("Ceci est un testbis");
            return valuation(j, j.getGameboard()[ligne][colonne].get_color());
        }
        if(j.getJump()<=0 && !sameColor){
            j.setHas_jumped((byte) 0);
            System.out.println("ok1\n");
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        System.out.println("ok2\n");
                        if (j.getGameboard()[lig][col].get_color() != j.getGameboard()[ligne][colonne].get_color() && j.getGameboard()[lig][col].get_color() !=-1) {
                            System.out.println("ok3\n");
                            j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if (j.getPossible_jump() != null) {
                                System.out.println("ok4\n");
                                for (int n = 0; n < j.getPossible_jump().length; n++) {
                                    if ((j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != j.getGameboard()[lig][col].get_color() && (j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != -1) {
                                        if (obl == 0)
                                            min = 0;
                                        inter = min(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true);
                                        if (min > inter)
                                            min = inter;
                                        inter = max(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim-1, false);
                                        if (min > inter)
                                            min = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        System.out.println("la ligne "+ lig +" la colonne "+ col+" [0]: "+j.getPossible_jump()[n][0] +" ||  et [1]"+ j.getPossible_jump()[n][1]);
                                        inter = min(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim, true);
                                        if (min > inter)
                                            min = inter;
                                        inter = max(j.copy(), j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim-1, false);
                                        if (min > inter)
                                            min = inter;
                                    }

                                }
                            }
                        }
                        //System.out.println("Getpossiblemove : " + j.getPossible_move()[0] + " / " + j.getPossible_move()[1]);
                        if (j.getPossible_move() != null && obl == 0) {
                            System.out.println("le nombre de move possible d'après pierre :" + j.getPossible_move().length+"\n");
                            for (int n = 0; n < j.getPossible_move().length; n++) {
                                System.out.println(inter+"\n");
                                inter = max(j.copy(), j.getPossible_move()[n][0], j.getPossible_move()[n][0], lim - 1, false);
                                if (min > inter)
                                    min = inter;
                            }
                        }

                    }

                }
            }
        }
        System.out.println("min : " + min + " - lim : " + lim);

        return min;
    }

    public int[] minMax(byte couleur, GameBoard j, int lim) {
        System.out.println("fonction : " + j.getJump()+"\n");

        Pion[][] plateau = j.getGameboard();
        int[] pos= {-1,-1,-1,-1,0,0};
        int[][] possible_jump = {{-1,-1}};
        int[][] possible_move = {{-1,-1}};
        int[][] poss;
        int max=0;
        int inter=0;
        if(j.getJump()>1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() == couleur && j.getGameboard()[lig][col] instanceof Etoile) {
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true);
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
                    }
                }
            }
        }
        else if(j.getJump()==1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        System.out.println("nul1");
                        if (j.getGameboard()[lig][col].get_color() == couleur && j.getGameboard()[lig][col] instanceof Etoile) {
                            System.out.println("nul2");
                            poss = j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if(poss!=null) {

                                for (int i = 0; i < poss.length; i++) {
                                    System.out.println("nul3"+poss[i][0]+poss[i][1]);
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim-1, false);
                                    if (max < inter) {
                                        System.out.println("nul4");
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
                    inter=max(j.copy(),poss[i][0],poss[i][1],lim, true);
                    if (max < inter) {
                        max = inter;
                        pos[0] = j.getMovedPawn()[0];
                        pos[1] = j.getMovedPawn()[1];
                        pos[2] = poss[i][0];
                        pos[3] = poss[i][1];
                        possible_move = j.getPossible_move();
                        possible_jump = j.getPossible_jump();
                    }
                    inter=max(j.copy(),poss[i][0],poss[i][1],lim, false);
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
            System.out.println(("ok1\n"));
            for (int ligne = 0; ligne < 7; ligne++) {
                for (int col = 0; col < 9; col++) {
                    if (plateau[ligne][col] != null) {
                        System.out.println(("ok peut etre 2\n"));
                        if (plateau[ligne][col].get_color() == couleur) {
                            System.out.println(("ok peut etre 3\n"));
                            poss = j.get_possibilities(plateau[ligne][col], ligne, col);
                            if (j.getPossible_move() != null) {
                                System.out.println(("ok peut etre 4\n"));
                                for (int s = 0; s < j.getPossible_move().length; s++) {
                                    inter = min(j.copy(), j.getPossible_move()[s][0], j.getPossible_move()[s][1], lim - 1, false);
                                    if (max < inter) {
                                        max = inter;
                                        pos[0] = ligne;
                                        pos[1] = col;
                                        pos[2] = poss[s][0];
                                        pos[3] = poss[s][1];
                                        possible_jump = j.getPossible_jump();
                                        possible_move = j.getPossible_move();
                                    }
                                }
                                if (j.getPossible_jump() != null) {
                                    //System.out.println(("ok peut etre 5"+j.getPossible_jump().length+"\n"));
                                    for (int s = 0; s < j.getPossible_jump().length; s++) {
                                        //System.out.println("less coordonnées : "+j.getPossible_jump()[s][0]+ "||"+ j.getPossible_jump()[s][1] + "il vient de : " + ligne + "||"+col);
                                        inter = max(j.copy(), j.getPossible_jump()[s][0], j.getPossible_jump()[s][1], lim, true);
                                        //System.out.println(("max jump : "+max+"\n"));
                                        //System.out.println(("inter jump : " + inter+"\n"));
                                        if (max < inter) {
                                            max = inter;
                                            pos[0] = ligne;
                                            pos[1] = col;
                                            pos[2] = poss[s][0];
                                            pos[3] = poss[s][1];
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
        }
        j.setPossible_move(possible_move);
        j.setPossible_jump(possible_jump);
        j.setSelection(pos[0],pos[1]);

        System.out.println("Mverenvoi : " +j.move(pos[2],pos[3]) + " pos[0]/ pos1 :" + pos[0] + pos[1]);
        pos[4]=j.getHas_jumped();
        pos[5]=j.getJump();
        return pos;
    }
}
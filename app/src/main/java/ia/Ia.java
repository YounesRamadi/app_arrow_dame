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
        for (int lig = 0; lig < 7; lig++) {
            for (int col = 0; col < 9; col++){
                if (j.getGameboard()[lig][col] != null){
                    j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                    if(j.getGameboard()[lig][col].get_color()==color){
                        score+=j.getPossible_jump().length*2;
                        score+=j.getPossible_move().length;
                    }
                    else{
                        score-=j.getPossible_jump().length*2;
                        score-=j.getPossible_move().length;
                    }
                    if(j.getGameboard()[lig][col].get_color()!=j.getGameboard()[lig][col].get_direction() && j.getGameboard()[lig][col].get_color()==color)
                        score+=20;
                    else if(j.getGameboard()[lig][col].get_color()==color && j.getGameboard()[lig][col] instanceof Etoile) {
                        if (j.getNb_B_stars() == 1 && color == 1) {
                            fEtoile = 0;
                            score += lig;
                        } else if (j.getNb_W_stars() == 1 && color == 0) {
                            fEtoile = 0;
                            score += 6 - lig;
                        } else if (j.getNb_B_stars() == 2 && color == 1) {
                            fEtoile = fEtoile - 2;
                            score += lig;
                        } else if (j.getNb_W_stars() == 2 && color == 0) {
                            fEtoile = fEtoile - 2;
                            score += 6 - lig;
                        } else if (j.getNb_B_stars() == 3 && color == 1) {
                            fEtoile--;
                            score += lig;
                        } else if (j.getNb_W_stars() == 3 && color == 0) {
                            fEtoile--;
                            score += 6 - lig;
                        }
                    }
                }
            }
        }
        return score;
    }
    public int max(GameBoard j, int ligne, int colonne, int lim){
        j.move(ligne, colonne);

        int max=0;
        int inter=0;
        int obl=0;
        int[][] poss;
        if(j.getHas_jumped()==1){

            poss=j.get_possibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                System.out.println("zob"+poss.length);
                for(int i=0; i<poss.length; i++){
                    inter=max(j,poss[i][0],poss[i][1],lim);
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
                                    inter = max(j, poss[i][0], poss[i][1], lim);
                                    if (max < inter)
                                        max = inter;
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
                                    inter = min(j, poss[i][0], poss[i][1], lim);
                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim==0 || j.getNb_W_stars()==0 || j.getNb_B_stars()==0){
            return valuation(j, (byte)((j.getGameboard()[ligne][colonne].get_color()+1)%2));
        }
        if(j.getJump()==0){
            j.setHas_jumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() != j.getGameboard()[ligne][colonne].get_color()) {
                            j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if (j.getPossible_jump() != null) {
                                for (int n = 0; n < j.getPossible_jump().length; n++) {
                                    if ((j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != j.getGameboard()[lig][col].get_color()) {
                                        if (obl == 0)
                                            max = 0;
                                        inter = max(j, j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim);
                                        if (max < inter)
                                            max = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        inter = max(j, j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim);
                                        if (max < inter)
                                            max = inter;
                                    }
                                }
                            }
                        }
                        if (j.getPossible_move() != null && obl == 0) {
                            for (int n = 0; n < j.getPossible_move().length; n++) {
                                inter = min(j, j.getPossible_move()[n][0], j.getPossible_move()[n][0], lim - 1);
                                if (max < inter)
                                    max = inter;
                            }
                        }
                    }
                }
            }
        }
        return max;
    }

    public int min(GameBoard j, int ligne, int colonne, int lim) {
        j.move(ligne, colonne);
        int min=0;
        int inter=0;
        int obl=0;
        int[][] poss;
        if(j.getHas_jumped()==1){
            poss=j.get_possibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    /*inter=min(j,poss[i][0],poss[i][1],lim);
                    if(min>inter){
                        min=inter;
                    }*/
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
                                    inter = min(j, poss[i][0], poss[i][1], lim);
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
                                    inter = max(j, poss[i][0], poss[i][1], lim);
                                    if (min > inter)
                                        min = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim==0 || j.getNb_W_stars()==0 || j.getNb_B_stars()==0){
            return valuation(j, j.getGameboard()[ligne][colonne].get_color());
        }
        if(j.getJump()==0){
            j.setHas_jumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].get_color() != j.getGameboard()[ligne][colonne].get_color()) {
                            j.get_possibilities(j.getGameboard()[lig][col], lig, col);
                            if (j.getPossible_jump() != null) {
                                for (int n = 0; n < j.getPossible_jump().length; n++) {
                                    if ((j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != j.getGameboard()[lig][col].get_color() && (j.getGameboard()[lig + (j.getPossible_jump()[n][0] - lig) / 2][col + (j.getPossible_jump()[n][1] - col) / 2].get_color()) != -1) {
                                        if (obl == 0)
                                            min = 0;
                                        inter = min(j, j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim);
                                        if (min > inter)
                                            min = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        inter = min(j, j.getPossible_jump()[n][0], j.getPossible_jump()[n][1], lim);
                                        if (min > inter)
                                            min = inter;
                                    }
                                }
                            }
                        }
                        if (j.getPossible_move() != null && obl == 0) {
                            for (int n = 0; n < j.getPossible_move().length; n++) {
                                inter = max(j, j.getPossible_move()[n][0], j.getPossible_move()[n][0], lim - 1);
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

    public int[] minMax(byte couleur, GameBoard j, int lim) {
        Pion[][] plateau = j.getGameboard();
        int[] pos= {-1,-1,-1,-1};
        int max=0;
        int inter=0;
        for (int ligne = 0; ligne < 7; ligne++) {
            for (int col = 0; col < 9; col++) {
                if (plateau[ligne][col] != null) {
                    if (plateau[ligne][col].get_color() == couleur) {
                        int[][] poss = j.get_possibilities(plateau[ligne][col], ligne, col);
                        if(poss!=null){
                            for (int s = 0; s < poss.length; s++) {
                                inter = min(j, poss[s][0], poss[s][1], lim);
                                if (max < inter) {
                                    max = inter;
                                    pos[0] = ligne;
                                    pos[1] = col;
                                    pos[2] = poss[s][0];
                                    pos[3] = poss[s][1];
                                }
                            }
                        }
                    }
                }
            }
        }
        return pos;
    }
}
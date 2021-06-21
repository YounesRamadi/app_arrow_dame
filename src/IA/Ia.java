package ia;
import controller.Etoile;
import controller.Jeu;
import controller.Pion;

public class Ia {

    public int valuation(Jeu j,byte color) {
        int fEtoile=3;
        int score=0;
        if(j.get_nb_et_noir()==0 && color==1)
            return 500;
        else if(j.get_nb_et_noir()==0 && color==0)
            return 0;
        else if(j.get_nb_et_blanc()==0 && color==0)
            return 500;
        else if(j.get_nb_et_blanc()==0 && color==1)
            return 0;
        for (int lig = 0; lig < 7; lig++) {
            for (int col = 0; col < 9; col++){
                j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                if(j.get_plateau()[lig][col].get_color()==color)
                    score+=1;
                else
                    score-=1;
                if(j.get_plateau()[lig][col].get_color()!=j.get_plateau()[lig][col].get_direction() && j.get_plateau()[lig][col].get_color()==color)
                    score+=10;
                else if(j.get_plateau()[lig][col].get_color()==color && j.get_plateau()[lig][col] instanceof Etoile){
                    if(j.get_nb_et_noir()==1 && color==1){
                        fEtoile=0;
                        score+=lig;
                    }
                    else if(j.get_nb_et_blanc()==1 && color==0){
                        fEtoile=0;
                        score+=6-lig;
                    }
                    else if(j.get_nb_et_noir()==2 && color==1){
                        fEtoile=fEtoile-2;
                        score+=lig;
                    }
                    else if(j.get_nb_et_blanc()==2 && color==0){
                        fEtoile=fEtoile-2;
                        score+=6-lig;
                    }
                    else if(j.get_nb_et_noir()==3 && color==1){
                        fEtoile--;
                        score+=lig;
                    }
                    else if(j.get_nb_et_blanc()==3 && color==0){
                        fEtoile--;
                        score+=6-lig;
                    }
                }
            }
        }
        return score;
    }

    public int max(Jeu j, int ligne, int colonne, int lim, int alpha, int beta){
        j.move(ligne, colonne);
        int max=0;
        int inter=0; //-600 --> alpha | 600 --> beta
        int obl=0;
        int[][] poss;
        if(j.get_hasJumped()==1){
            poss=j.get_possibilities(j.get_plateau()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=max(j,poss[i][0],poss[i][1],lim, alpha, beta);
                    if(max<=inter){
                            max=inter;
                        if(max>=beta){
                            return max;
                        }
                        if(alpha<max){
                            alpha=max;
                        }
                    }
                }
            }
        }
        if(j.get_jump()>1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if(j.get_plateau()[lig][col].get_color()==j.get_plateau()[ligne][colonne].get_color() && j.get_plateau()[lig][col] instanceof Etoile){
                        poss=j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                        for(int i=0; i<poss.length; i++){
                            inter=max(j,poss[i][0],poss[i][1],lim, alpha, beta);
                            if(max<=inter){
                                max=inter;
                                if(max>=beta){
                                    return max;
                                }
                                if(alpha<max){
                                    alpha=max;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(j.get_jump()==1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if(j.get_plateau()[lig][col].get_color()==j.get_plateau()[ligne][colonne].get_color() && j.get_plateau()[lig][col] instanceof Etoile){
                        poss=j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                        for(int i=0; i<poss.length; i++){
                            inter=min(j,poss[i][0],poss[i][1],lim, alpha, beta);
                            if(max<=inter){
                                max=inter;
                                if(max>=beta){
                                    return max;
                                }
                                if(alpha<max){
                                    alpha=max;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim==0 || j.get_nb_et_blanc()==0 || j.get_nb_et_noir()==0){
            max=valuation(j, (byte)((j.get_plateau()[ligne][colonne].get_color()+1)%2));
            return max;
        }
        if(j.get_jump()==0){
            j.set_hasJumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if(j.get_plateau()[lig][col].get_color()!=j.get_plateau()[ligne][colonne].get_color()){
                        j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                        if(j.get_possible_jump()!=null){
                            for(int n=0; n<j.get_possible_jump().length; n++){
                                if ((j.get_plateau()[lig + (j.get_possible_jump()[n][0] - lig)/2][col+(j.get_possible_jump()[n][1] - col)/2].get_color()) != j.get_plateau()[lig][col].get_color()){
                                    if(obl==0)
                                        max=0;
                                    inter=max(j,j.get_possible_jump()[n][0],j.get_possible_jump()[n][1],lim, alpha, beta);
                                    if(max<=inter){
                                        max=inter;
                                        if(max>=beta){
                                            return max;
                                        }
                                        if(alpha<max){
                                            alpha=max;
                                        }
                                    }
                                    obl=1;
                                }
                                else if(obl==0){
                                    inter=max(j,j.get_possible_jump()[n][0],j.get_possible_jump()[n][1],lim, alpha, beta);
                                    if(max<inter)
                                        max=inter;
                                }
                            }
                        }
                    }
                    if(j.get_possible_move()!=null && obl==0){
                        for(int n=0; n<j.get_possible_move().length; n++){
                            inter=min(j,j.get_possible_move()[n][0],j.get_possible_move()[n][0],lim-1, alpha, beta);
                            if(max<=inter){
                                max=inter;
                                if(max>=beta){
                                    return max;
                                }
                                if(alpha<max){
                                    alpha=max;
                                }
                            }
                        }
                    }
                    
                }
            }
        }
        return max;
    }

    public int min(Jeu j, int ligne, int colonne, int lim, int alpha, int beta) {
        j.move(ligne, colonne);
        int min=0;
        int inter=0;
        int obl=0;
        int[][] poss;
        if(j.get_hasJumped()==1){
            poss=j.get_possibilities(j.get_plateau()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=min(j,poss[i][0],poss[i][1],lim, alpha, beta);
                    if(min>inter){
                        min=inter;
                        if(min<=alpha){
                            return min;
                        }
                        if(beta>min){
                            beta=min;
                        }
                    }
                }
            }
        }
        if(j.get_jump()>1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if(j.get_plateau()[lig][col].get_color()==j.get_plateau()[ligne][colonne].get_color() && j.get_plateau()[lig][col] instanceof Etoile){
                        poss=j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                        for(int i=0; i<poss.length; i++){
                            inter=min(j,poss[i][0],poss[i][1],lim, alpha, beta);
                            if(min>inter){
                                min=inter;
                                if(min<=alpha){
                                    return min;
                                }
                                if(beta>min){
                                    beta=min;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if(j.get_jump()==1){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if(j.get_plateau()[lig][col].get_color()==j.get_plateau()[ligne][colonne].get_color() && j.get_plateau()[lig][col] instanceof Etoile){
                        poss=j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                        for(int i=0; i<poss.length; i++){
                            inter=max(j,poss[i][0],poss[i][1],lim, alpha, beta);
                            if(min>inter){
                                min=inter;
                                if(min<=alpha){
                                    return min;
                                }
                                if(beta>min){
                                    beta=min;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(lim==0 || j.get_nb_et_blanc()==0 || j.get_nb_et_noir()==0){
            min=valuation(j, j.get_plateau()[ligne][colonne].get_color());
            return min;
        }
        if(j.get_jump()==0){
            j.set_hasJumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if(j.get_plateau()[lig][col].get_color()!=j.get_plateau()[ligne][colonne].get_color()){
                        j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                        if(j.get_possible_jump()!=null){
                            for(int n=0; n<j.get_possible_jump().length; n++){
                                if ((j.get_plateau()[lig + (j.get_possible_jump()[n][0] - lig)/2][col+(j.get_possible_jump()[n][1] - col)/2].get_color()) != j.get_plateau()[lig][col].get_color()){
                                    if(obl==0)
                                        min=0;
                                    inter=min(j,j.get_possible_jump()[n][0],j.get_possible_jump()[n][1],lim, alpha, beta);
                                    if(min>inter){
                                        min=inter;
                                        if(min<=alpha){
                                            return min;
                                        }
                                        if(beta>min){
                                            beta=min;
                                        }
                                    }
                                    obl=1;
                                }
                                else if(obl==0){
                                    inter=min(j,j.get_possible_jump()[n][0],j.get_possible_jump()[n][1],lim, alpha, beta);
                                    if(min>inter){
                                        min=inter;
                                        if(min<=alpha){
                                            return min;
                                        }
                                        if(beta>min){
                                            beta=min;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(j.get_possible_move()!=null && obl==0){
                        for(int n=0; n<j.get_possible_move().length; n++){
                            inter=max(j,j.get_possible_move()[n][0],j.get_possible_move()[n][0],lim-1, alpha, beta);
                            if(min>inter){
                                min=inter;
                                if(min<=alpha){
                                    return min;
                                }
                                if(beta>min){
                                    beta=min;
                                }
                            }
                        }
                    }
                    
                }
            }
        }
        return min;
    }

    public int[] minMax(byte couleur, Jeu j, int lim) {
        Pion[][] plateau = j.get_plateau();
        int[] pos= {-1,-1};
        int max=0;
        int alpha=-600;
        int beta= 600;
        int inter= 0;
        for (int ligne = 0; ligne < 7; ligne++) {
            for (int col = 0; col < 9; col++) {
                if (plateau[ligne][col].get_color() == couleur) {
                    int[][] poss=j.get_possibilities(plateau[ligne][col],ligne,col);
                    for(int s=0; s<poss.length; s++){
                        inter=min(j,poss[s][0],poss[s][1],lim, alpha, beta);
                        if(max<inter){
                            max=inter;
                            alpha=max;
                            pos=poss[s];
                        }
                    }
                }
            }
        }
        return pos;
    }
}

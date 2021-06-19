package IA;
import controller.Etoile;
import controller.Jeu;
import controller.Pion;

public class Ia {

    private int points;

    public int valuation(Pion[][] tab) {

        return 0;
    }

    public int max(Jeu j, int ligne, int colonne, int lim){
        j.move(ligne, colonne);
        int max=0, inter=0, obl=0;
        int[][] poss;
        if(j.get_hasJumped()==1){
            poss=j.get_possibilities(j.get_plateau()[ligne][colonne], ligne, colonne);
            if(poss!=null){
                for(int i=0; i<poss.length; i++){
                    inter=max(j,poss[i][0],poss[i][1],lim);
                    if(max<inter){
                        max=inter;
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
                            inter=max(j,poss[i][0],poss[i][1],lim);
                            if(max<inter)
                                max=inter;
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
                            inter=min(j,poss[i][0],poss[i][1],lim);
                            if(max<inter)
                                max=inter;
                        }
                    }
                }
            }
        }
        if(lim==0){
            //valuation
        }
        if(j.get_jump()==0 && j.get_hasJumped()==0){
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++){
                    if(j.get_plateau()[lig][col].get_color()!=j.get_plateau()[ligne][colonne].get_color()){
                        j.get_possibilities(j.get_plateau()[lig][col], lig, col);
                        if(j.get_possible_jump()!=null){
                            for(int n=0; n<j.get_possible_jump().length; n++){
                                if ((j.get_plateau()[lig + (j.get_possible_jump()[n][0] - lig)/2][col+(j.get_possible_jump()[n][1] - col)/2].get_color()) != j.get_plateau()[lig][col].get_color()){
                                    if(obl==0)
                                        max=0;
                                    inter=max(j,j.get_possible_jump()[n][0],j.get_possible_jump()[n][1],lim);
                                    if(max<inter)
                                        max=inter;
                                    n=1;
                                }
                                else if(obl==0){
                                    inter=max(j,j.get_possible_jump()[n][0],j.get_possible_jump()[n][1],lim);
                                    if(max<inter)
                                        max=inter;
                                }
                            }
                        }
                    }
                    if(j.get_possible_move()!=null && obl==0){
                        for(int n=0; n<j.get_possible_move().length; n++){
                            inter=min(j,j.get_possible_move()[n][0],j.get_possible_move()[n][0],lim-1);
                            if(max<inter)
                                max=inter;
                        }
                    }
                    
                }
            }
        }
        return max;
    }

    public int min(Jeu j, int ligne, int colonne, int lim) {
        return 0;
    }

    public int scoreMoins(Pion[][] tab) {
        return 0;
    }

    public int[] MinMax(byte couleur, Jeu j, int lim) {
        Pion[][] plateau = j.get_plateau();
        int pos[]= {-1,-1};
        int max=0, inter=0;
        for (int ligne = 0; ligne < 7; ligne++) {
            for (int col = 0; col < 9; col++) {
                if (plateau[ligne][col].get_color() == couleur) {
                    int[][] poss=j.get_possibilities(plateau[ligne][col],ligne,col);
                    for(int s=0; s<poss.length; s++){
                        inter=min(j,poss[s][0],poss[s][1],lim);
                        if(max<inter)
                            max=inter;
                            pos=poss[s];
                    }
                    // direction 1 en bas, 0 en haut
                }
            }
        }
        return pos;
    }
}

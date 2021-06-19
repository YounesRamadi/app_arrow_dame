package controller;
import java.util.ArrayList;
import java.beans.ExceptionListener;

public class Jeu {
    private Pion plateau[][] = new Pion[7][9];
    private int tour; // nombre de tours actuels, commence à 0
    private int nb_et_noir; // nombre detoiles noir
    private int nb_et_blanc; // idem blanc
    private int jump; // nombre de jumps ennemis effectues <=> nombre de move de letoiles
    
    private int[][] possible_jump; // nombre de jump possible
    private int[][] possible_move; // nombre de move possible

    private byte has_jumped; // permet de savoir si un pion a deja saute un ennemi
    private int[] selection = new int[2]; // pion selectionne

    //constructeur par defaut
    public Jeu(){
        int i, j;
        // definition des variables de classe
        tour = 0;
        jump = 0;
        has_jumped = (byte) 0;
        selection[0] = -1;
        selection[1] = -1;

        /* creation du plateau de pion */

        int[] tempPlateau = new int[63]; // -1 existe pas, 0 NULL, 1 En, 2 Fn, 3 Eb, 4 Fb
        
        for(i=0; i<63;i++){
            if(i<3){
                j=2;
            } else if(i<6){
                j=1;
            } else if(i<9){
                j = -1;
            } else if (i<16){
                j=2;
            } else if(i < 18){
                j =-1;
            } else if(i == 26){
                j=-1;
            } else if( i == 36){
                j = -1;
            } else if(i <45){
                j= 0;
            } else if(i<47){
                j = -1;
            } else if( i< 54){
                j = 4;
            } else if( i < 57){
                j = -1;
            }
             else if( i<60){
                j = 3;
            } else if(i<63){
                j = 4;
            }else{
                j = -1;
            }
            tempPlateau[i] = j;

        }
        for(i=0;i<63;i++) {
            tempPlateau[i] = 0;
        }
        tempPlateau[40] = 4;
        tempPlateau[57] = 2;
        
        int a =0;
        j = 0;
        for(i=0;i<63;i++,a++){
            if(a%9 == 0){
                a=0;
            }
            switch(tempPlateau[i]){
                case -1:
                    plateau[j][a] = null;
                    break;

                case 1:
                    plateau[j][a] = new Etoile((byte)1,(byte)1);
                    break;
                case 2:
                    plateau[j][a] = new Fleche((byte)1,(byte)1);
                    break;
                case 3:
                    plateau[j][a] = new Etoile((byte)0,(byte)0);
                    break;
                case 4:
                    plateau[j][a] = new Fleche((byte)0,(byte)0);
                    break;
                default :
                    plateau[j][a] = new Pion();
                    break;
            }
            if((i+1) %9 == 0){
                j++;
                
            }
        }
        define_nbNEt();
        System.out.println("Nombre detoiles blanches : " + nb_et_blanc + ", noir : " +nb_et_noir);
        // fin de creation du plateau de pion
        this.get_possibilities(plateau[6][3], 6, 3);
        this.display();

    }
    private void define_nbNEt(){
        nb_et_blanc = 0;
        nb_et_noir = 0;
        for(int i=0; i<7;i++){
            for(int j=0; j<9; j++){
                if(plateau[i][j] != null && plateau[i][j] instanceof Etoile){ // etoile
                    if(plateau[i][j].get_couleur() == 1){ //noir
                        nb_et_noir++;
                    }
                    else{
                        nb_et_blanc++;
                    }
                }
            }
        }
    }
    public Jeu(Pion plateau[][]){


        define_nbNEt();
    }

    public int get_nb_et_noir(){
        return nb_et_noir;
    }

    public int get_nb_et_blanc(){
        return nb_et_blanc;
    }

    public Pion[][] get_plateau(){
        return plateau;
    }

    public Pion get_pion (int posx, int posy){
        return plateau[posx][posy];
    }
    public byte get_hasJumped(){
        return has_jumped;
    }
    public int get_jump(){
        return jump;
    }
    public byte end_tour(){
        jump=0;
        possible_jump = null;
        possible_move = null;
        has_jumped = (byte)0;
        selection = null;
        if (nb_et_noir == 0 || nb_et_blanc == 0){
            return (byte)1;
        }
        tour++;
        return (byte)0;
    }

    public void display(){
        for(int i=0; i<7;i++){
            for(int j=0; j<9;j++){
            
                if(plateau[i][j] != null){
                    System.out.print(plateau[i][j]);

                   
                }
                else{
                    System.out.print("  ");
                }
                System.out.print(" | ");
            }
            System.out.println("");
        }
    }
    // check_selection permet de verifier que l'utilisateur puisse prendre la piece
    //honetement on sait pas ce qu'elle fait
    public int check_selection(int posx, int posy){
        if(plateau[posx][posy] == null || plateau[posx][posy].couleur == -1){
            return -1;
        }
        int actual_color = plateau[posx][posy].get_couleur();
        //on commence par les blancs
        // tour % 2 == 0 -> blanc
        if(actual_color != tour%2){
            return -1;
        }
        get_possibilities(plateau[posx][posy], posx, posy);
        if(possible_jump != null){
            return 0;
        }
        for(int i=0; i<7;i++){
            for(int j=0; j<9; j++){
                if(plateau[i][j].get_couleur() == actual_color && !(i!= posx || j !=posy)){
                    get_possibilities(plateau[i][j], i, j);
                    if(possible_jump != null){
                        possible_jump = null;
                        return -1;
                    }
                }
            }
        }
        return 0;
    }
    public int[][] get_possibilities(Pion p, int posx, int posy){
        

        selection[0] = posx;
        selection[1] = posy;
        if(plateau[posx][posy] instanceof Etoile){
            return get_possibilitiesEtoile(plateau[posx][posy], posx, posy);
        }
            return get_possibilitiesFleche(plateau[posx][posy], posx, posy);
        
    }

    // retourne 0 si cest vide
    // retourne 1 si cest la même couleur
    // retourne 2 si la couleur est differente
    // retourne -1 si le pion est en dehors de la grille
    private int check_specified_pawn(int posPionx, int posPiony, int posFinalx, int posFinaly){
        // toutes les positions 
        if(posPionx < 0 || posPionx >= 7 || posPiony < 0 || posPiony >= 9)
            return -1;
        if(posFinalx < 0 || posFinalx >= 7 || posFinaly < 0 || posFinaly >= 9)
            return -1;
        if(plateau[posFinalx][posFinaly] ==null)
            return -1;
        if(plateau[posFinalx][posFinaly].couleur == -1)
            return 0;
        if(plateau[posPionx][posPiony].couleur == plateau[posFinalx][posFinaly].couleur)
            return 1;
        return 2;
        // check couleurs

    }

    private int[][] get_possibilitiesEtoile(Pion p, int posx, int posy){

        ArrayList<int[]> arl=new ArrayList<int[]>();
        ArrayList<int[]> ar2=new ArrayList<int[]>();
        int[] liste_int = new int[2];
        int[] tmp = new int[2];
        int indexMove = 0;
        int indexJump = 0;
        if (p.direction == 0){
            //jumps
            // != -1 -> > 0 && le ==-1 == 0
            if(check_specified_pawn(posx, posy, posx-1, posy) >0  && check_specified_pawn(posx, posy, posx-2, posy) == 0){
                // pion P() -> 0
                tmp[0] = posx-2;
                tmp[1] = posy;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx-2][posy] = new debug();
            }
            if(check_specified_pawn(posx, posy, posx+2, posy) == 0  && check_specified_pawn(posx, posy, posx+1, posy) > 0){
            
                tmp[0] = posx;
                tmp[1] = posy+2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx][posy+2] = new debug();
            }
            if(check_specified_pawn(posx, posy, posx, posy-2) == 0  && check_specified_pawn(posx, posy, posx, posy-1) > 0){
            
                tmp[0] = posx;
                tmp[1] = posy-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx][posy-2] = new debug();
            }
            if(check_specified_pawn(posx, posy, posx-2, posy-2) == 0  && check_specified_pawn(posx, posy, posx-1, posy-1) > 0){
         
                tmp[0] = posx-2;
                tmp[1] = posy-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx-2][posy-2] = new debug();            
            }
            //move
            if(indexJump == 0){
                if(check_specified_pawn(posx, posy, posx-1, posy) == 0){
                 // pion P() -> 0
                    tmp[0] = posx-1;
                    tmp[1] = posy;
                    arl.add(indexMove++,  tmp);
                    //debugage a enlever vitezef
                    plateau[posx-1][posy] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx, posy+1) == 0){
                    tmp[0] = posx;
                    tmp[1] = posy+1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx][posy+1] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx, posy-1) == 0){
                    tmp[0] = posx;
                    tmp[1] = posy-1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx][posy-1] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx-1, posy-1) == 0){
                    tmp[0] = posx-1;
                    tmp[1] = posy-1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx-1][posy-1] = new debug();            
                }
            }
        }
        else {
            //jumps
            // != -1 -> > 0 && le ==-1 -> == 0
            if(check_specified_pawn(posx, posy, posx+2, posy) == 0  && check_specified_pawn(posx, posy, posx+1, posy) > 0){
             // pion P() -> 0
                tmp[0] = posx+2;
                tmp[1] = posy;
                arl.add(indexMove++,  tmp);
                //debugage a enlever vitezef
                plateau[posx+2][posy] = new debug();
            }
            if(check_specified_pawn(posx, posy, posx, posy-2) == 0  && check_specified_pawn(posx, posy, posx, posy-1) > 0){
                tmp[0] = posx;
                tmp[1] = posy-2;
                arl.add(indexMove++, tmp);
                //debugage a enlever vitezef
                plateau[posx][posy-2] = new debug();
            }
            if(check_specified_pawn(posx, posy, posx, posy+2) == 0  && check_specified_pawn(posx, posy, posx, posy+1) > 0){
                tmp[0] = posx;
                tmp[1] = posy+2;
                arl.add(indexMove++, tmp);
                //debugage a enlever vitezef
                plateau[posx][posy+2] = new debug();
            }
            if(check_specified_pawn(posx, posy, posx+2, posy+2) == 0  && check_specified_pawn(posx, posy, posx+1, posy+1) > 0){
                tmp[0] = posx+2;
                tmp[1] = posy+2;
                arl.add(indexMove++, tmp);
                //debugage a enlever vitezef
                plateau[posx+2][posy+2] = new debug();            
            }
            //move
            if(indexJump == 0){
                if(check_specified_pawn(posx, posy, posx+1, posy) == 0){ // pion P() -> 0
                    tmp[0] = posx+1;
                    tmp[1] = posy;
                    arl.add(indexMove++,  tmp);
                    //debugage a enlever vitezef
                    plateau[posx+1][posy] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx, posy-1) == 0){
                    tmp[0] = posx;
                    tmp[1] = posy-1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx][posy-1] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx, posy+1) == 0){
                
                    tmp[0] = posx;
                    tmp[1] = posy+1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx][posy+1] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx+1, posy+1) == 0){
                    tmp[0] = posx+1;
                    tmp[1] = posy+1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx+1][posy+1] = new debug();            
                }
            }
        }

        possible_move= arl.toArray(new int[0][0]);
        possible_jump = ar2.toArray(new int[0][0]);
        int[][] retour = new int[indexJump+indexMove][2];
        for(int i=0; i<indexJump+indexMove;i++){
            if(i<indexMove && indexMove != 0){
                retour[i] = possible_move[i];
            }
            if(i>=indexMove && indexJump !=0){
                retour[i] = possible_jump[i-indexMove];
            }
        }
        return retour;

    }
    private int[][] get_possibilitiesFleche(Pion p, int posx, int posy){
        ArrayList<int[]> arl=new ArrayList<int[]>(); // moves
        ArrayList<int[]> ar2=new ArrayList<int[]>(); //jump
        int[] liste_int = new int[2];
        int[] tmp = new int[2];
        int indexMove = 0;
        int indexJump = 0;

        int possibleEnemyJump =0; // est-ce que le pion peut jump un ennemi
        //cest un flag
        
        if (p.direction == 0){
            //jumps
            if(check_specified_pawn(posx, posy, posx-1, posy) > 0  && check_specified_pawn(posx, posy, posx-1, posy-1) == 0){// pion P() -> 0
                tmp[0] = posx-2;
                tmp[1] = posy;
                ar2.add(indexJump++,  tmp);
                //debugage a enlever vitezef
                plateau[posx-2][posy] = new debug();
                if(plateau[posx-1][posy].couleur != plateau[posx][posy].couleur){
                    possibleEnemyJump = 1;
                }
            }
            if(check_specified_pawn(posx, posy, posx, posy+2) == 0  && check_specified_pawn(posx, posy, posx, posy+1) > 0 && has_jumped == (byte)1){
                tmp[0] = posx;
                tmp[1] = posy+2;
                ar2.add(indexJump++, tmp);
                //debugage a enlever vitezef
                plateau[posx][posy+2] = new debug();
                if(plateau[posx][posy+1].couleur != plateau[posx][posy].couleur){
                    possibleEnemyJump = 1;
                }
            }
            if(check_specified_pawn(posx, posy, posx, posy-2) == 0  && check_specified_pawn(posx, posy, posx, posy-1) > 0 && has_jumped == (byte)1){
                tmp[0] = posx;
                tmp[1] = posy-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx][posy-2] = new debug();
                if(plateau[posx][posy-1].couleur != plateau[posx][posy].couleur){
                    possibleEnemyJump = 1;
                }
            }
            if(check_specified_pawn(posx, posy, posx-2, posy-2) == 0  && check_specified_pawn(posx, posy, posx-1, posy-1) > 0){
                tmp[0] = posx-2;
                tmp[1] = posy-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx-2][posy-2] = new debug();
                if(plateau[posx-1][posy].couleur != plateau[posx][posy].couleur){
                    possibleEnemyJump = 1;
                }            
            }
            if(indexJump == 0){
            //move
                if(check_specified_pawn(posx, posy, posx-1, posy) == 0){ // pion P() -> 0
                    tmp[0] = posx-1;
                    tmp[1] = posy;
                    arl.add(indexMove++,  tmp);
                    //debugage a enlever vitezef
                    plateau[posx-1][posy] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx-1, posy-1) == 0){
                    tmp[0] = posx-1;
                    tmp[1] = posy-1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx-1][posy-1] = new debug();            
                }
            }
        }
        else {
            //jumps
            if(check_specified_pawn(posx, posy, posx+2, posy) == 0  && check_specified_pawn(posx, posy, posx+1, posy) > 0){ // pion P() -> 0
                tmp[0] = posx+2;
                tmp[1] = posy;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx+2][posy] = new debug();
                if(plateau[posx+1][posy].couleur != plateau[posx][posy].couleur){
                    possibleEnemyJump = 1;
                }
            }
            if(check_specified_pawn(posx, posy, posx, posy-2) == 0  && check_specified_pawn(posx, posy, posx, posy-1) > 0 && has_jumped == (byte)1){
                tmp[0] = posx;
                tmp[1] = posy-2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx][posy-2] = new debug();
                if(plateau[posx][posy-1].couleur != plateau[posx][posy].couleur){
                    possibleEnemyJump = 1;
                }
            }
            if(check_specified_pawn(posx, posy, posx, posy+2) == 0  && check_specified_pawn(posx, posy, posx, posy+1) > 0&& has_jumped == (byte)1){
                tmp[0] = posx;
                tmp[1] = posy+2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx][posy+2] = new debug();
                if(plateau[posx][posy+1].couleur != plateau[posx][posy].couleur){
                    possibleEnemyJump = 1;
                }
            }
            if(check_specified_pawn(posx, posy, posx+2, posy+2) == 0  && check_specified_pawn(posx, posy, posx+1, posy+1) > 0){
                tmp[0] = posx+2;
                tmp[1] = posy+2;
                ar2.add(indexJump++, tmp);                //debugage a enlever vitezef
                plateau[posx+2][posy+2] = new debug();            
            }
            //move
            if(indexJump == 0){
                if(check_specified_pawn(posx, posy, posx+1, posy) == 0){ // pion P() -> 0
                    tmp[0] = posx+1;
                    tmp[1] = posy;
                    arl.add(indexMove++,  tmp);
                    //debugage a enlever vitezef
                    plateau[posx+1][posy] = new debug();
                }
                if(check_specified_pawn(posx, posy, posx+1, posy+1) == 0){
                    tmp[0] = posx+1;
                    tmp[1] = posy+1;
                    arl.add(indexMove++, tmp);
                    //debugage a enlever vitezef
                    plateau[posx+1][posy+1] = new debug();            
                }
            }
        }

        possible_move= arl.toArray(new int[0][0]);
        possible_jump = ar2.toArray(new int[0][0]);
        int[][] retour = new int[indexJump+indexMove][2];
        for(int i=0; i<indexJump+indexMove;i++){
            if(i<indexMove && indexMove != 0){
                retour[i] = possible_move[i];
            }
            if(i>=indexMove && indexJump !=0){
                retour[i] = possible_jump[i-indexMove];
            }
        }
        return retour;
    }


    // fonction pour deplacer les pions
    // return -1 si il a pas bougé
    // return 0 si il  a bougé
    public int move(int posx, int posy){
        
        if(check_specified_pawn(posx, posy,selection[0], selection[1]) == -1){
            System.err.println("Tried to move a non-existing pawn");
            return -1;
        }
        // commence par s'il y a des jumps
        for(int i=0; i<possible_jump.length;i++){
            for(int j=0; j<possible_jump[i].length; j++){
                if (possible_jump[i][0]==posx && possible_jump[i][1] == posy){
                    int distanceX = (posx - selection[0]) / 2;
                    int distanceY = (posy - selection[1]) / 2;
                    if (plateau[distanceX + selection[0]][distanceY + selection[1]].get_couleur() != plateau[selection[0]][selection[1]].get_couleur()){
                        jump++;
                    }
                    if(plateau[posx][posy] instanceof Etoile){
                        if(jump >0){
                            jump--;
                        }else{
                            return -1;
                        }
                        
                    }
                    has_jumped = (byte)1;
                    plateau[posx][posy]=plateau[selection[0]][selection[1]];
                    plateau[selection[0]][selection[1]]= new Pion();
                    possible_move = null;
                }
                if(possible_move[i][0]==posx && possible_jump[i][1] == posy){
                    
                }
            }
        }
        return 1;
    }

}
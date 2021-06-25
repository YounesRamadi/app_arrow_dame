/**
 * Classe de Gestion d'une partie d'Apadnom
 * @author Sisirpz
 * @version 0.2.1
 */

package controller;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard implements Parcelable{

    private Pion[][] gameboard;
    private int nb_W_stars;
    private int nb_B_stars;
    private int turn;

    private byte has_jumped;
    private int jump;

    private int[][] possible_jump;
    private  int[][] possible_move;

    public int[] getSelection() {
        return selection;
    }


    private int[] selection = new int[2]; // le pion selectionne
    private int[] movedPawn = new int[2]; // le dernier pion

    public int[] getMustJump() {
        return mustJump;
    }

    public void setMustJump(int a, int b) {
        this.mustJump[0] = a;
        this.mustJump[1] = b;
    }

    private int[] mustJump = new int[2];



    public int[] getMovedPawn() {
        return movedPawn;
    }


    public void setLastPosition(int lastPositionx, int lastPositiony) {
        this.lastPosition[0] = lastPositionx;
        this.lastPosition[1] = lastPositiony;
    }

    private int[] lastPosition = new int[2];


    public int[][] getJumpableEnnemies() {
        return jumpableEnnemies;
    }

    private int[][] jumpableEnnemies;

    private Context context;
    private Toast toast;

    public ArrayList<int[][]> getJumpedPawn() {
        return jumpedPawn;
    }

    public void setJumpedPawn(ArrayList<int[][]> jumpedPawn) {
        this.jumpedPawn = (ArrayList) jumpedPawn.clone();
    }

    private ArrayList<int[][]> jumpedPawn = new ArrayList<int[][]>();
    public GameBoard(GameBoard g, Context context){
        String entree = "03b03a07b25o07d03c03d";

        this.context = context;
        this.gameboard = makeGameBoard(entree);
        for(int i = 0; i < 7; i++){
            for(int j = 0; i < 0; j++){
                this.gameboard[i][j] = g.gameboard[i][j];
            }
        }
        this.nb_B_stars = g.getNb_B_stars();
        this.nb_W_stars = g.getNb_W_stars();
    }
    /**
     * Constructeur de la classe
     * @param context contexte pour la couche graphique (getContext())
     */
    public GameBoard(Context context) {
        // initGameBoard();
        String entree = "03b03a07b25o07d03c03d";

        this.context = context;
        this.gameboard = makeGameBoard(entree);
        setNb_stars();
    }

    public GameBoard(String ch, Context context) {
        this.context = context;
        this.gameboard = makeGameBoard(ch);
        setNb_stars();
    }

    protected GameBoard(Parcel in) {
        turn = in.readInt();
        nb_W_stars = in.readInt();
        nb_B_stars = in.readInt();
        has_jumped = in.readByte();
        jump = in.readInt();
        selection = in.createIntArray();
        movedPawn = in.createIntArray();
        lastPosition = in.createIntArray();
    }

    public static final Creator<GameBoard> CREATOR = new Creator<GameBoard>() {
        @Override
        public GameBoard createFromParcel(Parcel in) {
            return new GameBoard(in);
        }

        @Override
        public GameBoard[] newArray(int size) {
            return new GameBoard[size];
        }
    };

    public Pion[][] getGameboard() {
        return gameboard;
    }

    public byte getHas_jumped() {
        return has_jumped;
    }

    public void setHas_jumped(byte j) {
        has_jumped = j;
    }


    public int[][] getPossible_move() {
        if (possible_move == null) {
            return new int[0][0];
        }
        return  possible_move;
    }
    /**
     * Permet de copier l'objet / equivalent de clone
     * @return un objet GameBoard identique
     */
    public GameBoard copy(){
        GameBoard retour = new GameBoard(this.context);
        Pion[][] p= new Pion[7][9];
        for(int i=0; i<gameboard.length;i++){
            for(int j=0;j<gameboard[i].length;j++){
                if(gameboard[i][j]==null){
                    gameboard[i][j]=null;
                }
                else
                    p[i][j] = gameboard[i][j].copy();
            }
        }
        retour.setHas_jumped(has_jumped);
        retour.setNb_B_stars(nb_B_stars);
        retour.setNb_W_stars(nb_W_stars);
        retour.setPossible_jump(possible_jump);
        retour.setPossible_move(possible_move);
        retour.setJump(jump);
        retour.setLastPosition(lastPosition[0],lastPosition[1]);
        retour.setGameboard(p);
        retour.setSelection(this.selection[0], this.selection[1]);
        retour.set_movedPawn(movedPawn[0], movedPawn[1]);
        retour.setJumpedPawn(jumpedPawn);
        return retour;
    }

    private void setGameboard(Pion[][] p) {
        this.gameboard = p;
    }

    public void setPossible_move(int[][] possible_move) {
        if(possible_move == null){
            this.possible_move = null;
        }else {
            this.possible_move = possible_move.clone();
        }
    }

    public int[][] getPossible_jump() {
        if (possible_jump == null) {
            return new int[0][0];
        }
        return possible_jump;
    }

    public void setPossible_jump(int[][] possible_jump) {
        if(possible_jump == null){
            this.possible_jump = null;
        }else {
            this.possible_jump = possible_jump.clone();
        }
    }

    public void setNb_W_stars(int nb_W_stars) {
        this.nb_W_stars = nb_W_stars;
    }
    public void setNb_B_stars(int nb_B_stars) {
        this.nb_B_stars = nb_B_stars;
    }


    public void setSelection(int x, int y){
        selection[0] = x;
        selection[1] = y;
    }


    public void initGameBoard() {
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
        if (p != null)
            gameboard[x][y] = p;
    }

    public int getNb_W_stars(){
        //System.out.println("b_stars :" + nb_B_stars);
        return nb_W_stars;
    }

    public int getJump() {
        return jump;
    }

    public void setJump(int jump) {
        this.jump = jump;
    }

    public Pion[][] makeGameBoard(String ch) {
        int[] tab = new int[63];
        int i=0;
        int j=0;
        int oc = 0;
        int val=0;
        int p=0;
        String tmp = new String();
        Pion[][] tempgameboard = new Pion[7][9];

        for (i=0;i<ch.length();i++){
            if ((i%3)==0){
                oc = Integer.parseInt(ch.substring(i, i+2));
            }
            else if (i%3==2){
                tmp = ch.substring(i, i+1);
                switch(tmp){
                    case("a"):
                        val = 1;
                        break;
                    case("b"):
                        val=2;
                        break;
                    case("c"):
                        val=3;
                        break;
                    case("d"):
                        val=4;
                        break;
                    case("o"):
                        val=0;
                        break;
                    default:
                        val=-1;
                }
                for (p=0; p < oc; p++, j++)
                {
                    if(j==6 || j==7 || j==8 || j==16 || j==17 || j==26 || j==36 || j==45 || j==46 || j==54 || j==55 || j==56 )
                    {
                        tab[j]=-1;
                        p--;
                    }
                    else {
                        tab[j]=val;
                    }
                }
            }
        }

        int a = 0;
        j = 0;
        for(i=0;i<63;i++,a++){
            if(a%9 == 0){
                a=0;
            }
            switch(tab[i]){
                case -1:
                    tempgameboard[j][a] = null;
                    break;

                case 1:
                    tempgameboard[j][a] = new Etoile((byte)1,(byte)1);
                    break;
                case 2:
                    tempgameboard[j][a] = new Fleche((byte)1,(byte)1);
                    break;
                case 3:
                    tempgameboard[j][a] = new Etoile((byte)0,(byte)0);
                    break;
                case 4:
                    tempgameboard[j][a] = new Fleche((byte)0,(byte)0);
                    break;
                default :
                    tempgameboard[j][a] = new Pion();
                    break;
            }
            if((i+1) % 9 == 0){
                j++;

            }
        }
        return tempgameboard;
    }

    public int getNb_B_stars() {
        // System.out.println("b_stars :" + nb_B_stars);
        return nb_B_stars;
    }

    /**
     * Compte le nombre d'etoiles sur le plateau de jeu
     */
    public void setNb_stars() {
        int n = 0;
        int a = 0;
        Pion p;

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 9; j++) {
                p = gameboard[i][j];
                if (p instanceof Etoile){
                    if(p.get_color() == 0){
                        n++;
                    }else{
                        a++;
                    }
                }

            }
        }
        nb_W_stars = n;
        nb_B_stars = a;
    }


    // check_selection permet de verifier que l'utilisateur puisse prendre la piece
    // honetement on sait pas ce qu'elle fait

    /**
     *
     * @param x position en x du pion
     * @param y position en Y du pion
     * @param turn numero du tour actuel
     * @param checkforjump flag (toujours mettre a 1)
     * @return
     */
    public int check_selection(int x, int y, int turn, int checkforjump) {
        // on commence par regarder que les donnes envoyees sont bonnes
        if (x < 0 || x >= 7 || y < 0 || y >= 9) {
            toast = Toast.makeText(context, "Wtf args are you sending bruh ?", Toast.LENGTH_SHORT);
            toast.show();
            //System.err.println("Wtf args are you sending bruh ?");
            return -1;
        }
        // est ce que le pion choisi est de la bonne couleur
        if (gameboard[x][y].get_color() != turn % 2) {
            toast = Toast.makeText(context, "That's not your turn!", Toast.LENGTH_SHORT);
            toast.show();
            //System.err.println("That's not your pawn!");
            return -1;
        }


        int actual_color = gameboard[x][y].get_color();
        get_possibilities(gameboard[x][y], x, y);

        // flag checkforjump inutile

        //on regarde si lorsque l'on choisit une fleche
        // et que celle-ci ne peut pas sauter
        // qu'aucune autre fleche ne peut sauter
        if (has_jumped == (byte) 0) {
            if (checkforjump == 1 && gameboard[x][y] instanceof Fleche && has_jumped == (byte) 0) {
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 9; j++) {

                        if (gameboard[i][j] == null) {
                            continue;
                        }
                        // si le pion est de la couleur de la fleche
                        if (gameboard[i][j].get_color() == actual_color) {
                            // et que le pion n'est pas le pion teste
                            if (!(i == x && j == y)) {

                                // on regarde si il peut jump un ennemi et que cest une fleche
                                if (gameboard[i][j] instanceof Fleche && canJumpEnnemy(i, j) && !canJumpEnnemy(x, y)) {
                                    System.err.println("An other pawn can jump, impossible to move this arrow");
                                    setMustJump(i, j);
                                    return 1;
                                }
                            }
                        }
                    }
                }
            }
        }

                // si cest une etoile
            // on verifie juste son
        if (gameboard[x][y] instanceof Etoile) {
            if (jump < 1) {
                toast = Toast.makeText(context, "You can't move this star!", Toast.LENGTH_SHORT);
                toast.show();
                System.err.println("You can't move this star!");
                return -1;
            }
            return 0;
        } else {
            if (movedPawn != null) {
                if (gameboard[movedPawn[0]][movedPawn[1]] instanceof Etoile) {
                    toast = Toast.makeText(context, "You can't move this arrow! ( you just moved a star )", Toast.LENGTH_SHORT);
                    toast.show();
                    System.err.println("You can't move this arrow! ( you just moved a star )");
                    return -1;
                } else {
                    if (has_jumped == (byte) 0) {
                        return 0;
                    } else {
                        if ((x == movedPawn[0]) && (y == movedPawn[1])) {
                            return 0;
                        } else {
                            toast = Toast.makeText(context, "That's not the arrow you just moved!", Toast.LENGTH_SHORT);
                            toast.show();
                            System.err.println("That's not the arrow you just moved!");
                            return -1;
                        }
                    }
                }
            }
        }
        return 0;
    }



    // retuorne 0 si cest vide
    // retourne 1 si cest la même color
    // retourne 2 si la color est differente
    // retourne -1 si le pion est en dehors de la grille

    /**
     * Compare 2 pions en fonction de leurs couleurs
     * @param posPionx position en x du pion A
     * @param posPiony position en y du pion A
     * @param posFinalx position en x du pion B
     * @param posFinaly position en y du pion B
     * @return 0 si une des cases Pion est vide, 1 si c'est la meme couleur, 2 si elle est differente et -1 en cas d'erreur
     */
    private int check_specified_pawn(int posPionx, int posPiony, int posFinalx, int posFinaly){
        // toutes les positions sont dans le tableau ?
        if(posPionx < 0 || posPionx >= 7 || posPiony < 0 || posPiony >= 9){
            //System.err.println("PositionX and Position Y of the target pawn is out of the board.");
            return -1;
        }
        if(posFinalx < 0 || posFinalx >= 7 || posFinaly < 0 || posFinaly >= 9){
            //System.err.println("PositionX and Position Y of the checked pawn is out of the board.");
            return -1;
        }
        // si le pion choisi ne correspond pas à une case cachée
        if(gameboard[posFinalx][posFinaly] ==null || gameboard[posPionx][posPiony] ==null){
            //System.err.println("Target pawn is out of the board but still in the array");
            return -1;
        }
        //si la case choisit est une case vide
        if(gameboard[posFinalx][posFinaly].get_color() == -1 ||gameboard[posPionx][posPiony].get_color()==-1)
            return 0;
        // on compare les couleurs des deux pions
        if(gameboard[posPionx][posPiony].get_color() == gameboard[posFinalx][posFinaly].get_color())
            return 1;
        return 2;
        // check colors

    }


    /**
     * Permet d'obtenir la liste des possibilites de mouvement d'un Pion
     * @param p Fleche a verifier
     * @param x sa position en x
     * @param y sa position en y
     * @return liste de possibilites de mouvement (null si aucun mouvement)
     */
    public int[][] get_possibilities(Pion p, int x, int y){

        //on garde en memoire le pion selectionne
        selection[0] = x;
        selection[1] = y;
        //en fonction du pion on appelle une fonction differente
        // les mouvements sont differents
        if(gameboard[x][y] instanceof Etoile && jump > 0){
            return get_possibilitiesStar(gameboard[x][y], x, y);
        }else if(gameboard[x][y] instanceof Fleche){
            return get_possibilitiesArrow(gameboard[x][y], x, y);
        }
        return null;

    }
    /**
     * Permet d'obtenir la liste des possibilites de mouvement d'une étoile
     * @param p Etoile à verifier
     * @param x sa position en x
     * @param y sa position en y
     * @return liste de possibilites de mouvement
     */
    private int[][] get_possibilitiesStar(Pion p, int x, int y){

        if (jump ==0){
            return null;
        }

        ArrayList<int[]> arl=new ArrayList<int[]>();
        ArrayList<int[]> ar2=new ArrayList<int[]>();
        int[] liste_int = new int[2];
        int[] tmp = new int[2];
        int indexMove = 0;
        int indexJump = 0;
        if (p.get_direction() == 0){
            //jumps
            // != -1 -> > 0 && le ==-1 == 0
            if(check_specified_pawn(x, y, x-1, y) >0  && check_specified_pawn(x, y, x-2, y) == 0){
                // pion P() -> 0
                tmp = new int[2];
                tmp[0] = x-2;
                tmp[1] = y;
                ar2.add(indexJump++, tmp);
            }
            if(check_specified_pawn(x, y, x, y+2) == 0  && check_specified_pawn(x, y, x, y+1) > 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y+2;
                ar2.add(indexJump++, tmp);
            }
            if(check_specified_pawn(x, y, x, y-2) == 0  && check_specified_pawn(x, y, x, y-1) > 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y-2;
                ar2.add(indexJump++, tmp);
            }
            if(check_specified_pawn(x, y, x-2, y-2) == 0  && check_specified_pawn(x, y, x-1, y-1) > 0){
                tmp = new int[2];
                tmp[0] = x-2;
                tmp[1] = y-2;
                ar2.add(indexJump++, tmp);
            }
            //move

            if(check_specified_pawn(x, y, x-1, y) == 0){
                // pion P() -> 0
                tmp = new int[2];
                tmp[0] = x-1;
                tmp[1] = y;
                arl.add(indexMove++,  tmp);
            }
            if(check_specified_pawn(x, y, x, y+1) == 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y+1;
                arl.add(indexMove++, tmp);
            }
            if(check_specified_pawn(x, y, x, y-1) == 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y-1;
                arl.add(indexMove++, tmp);
            }
            if(check_specified_pawn(x, y, x-1, y-1) == 0){
                tmp = new int[2];
                tmp[0] = x-1;
                tmp[1] = y-1;
                arl.add(indexMove++, tmp);
            }

        }
        else {
            //jumps
            // != -1 -> > 0 && le ==-1 -> == 0
            if(check_specified_pawn(x, y, x+2, y) == 0  && check_specified_pawn(x, y, x+1, y) > 0){
                // pion P() -> 0
                tmp = new int[2];
                tmp[0] = x+2;
                tmp[1] = y;
                arl.add(indexMove++,  tmp);
            }
            if(check_specified_pawn(x, y, x, y-2) == 0  && check_specified_pawn(x, y, x, y-1) > 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y-2;
                arl.add(indexMove++, tmp);
            }
            if(check_specified_pawn(x, y, x, y+2) == 0  && check_specified_pawn(x, y, x, y+1) > 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y+2;
                arl.add(indexMove++, tmp);
            }
            if(check_specified_pawn(x, y, x+2, y+2) == 0  && check_specified_pawn(x, y, x+1, y+1) > 0){
                tmp = new int[2];
                tmp[0] = x+2;
                tmp[1] = y+2;
                arl.add(indexMove++, tmp);
            }
            //move

            if(check_specified_pawn(x, y, x+1, y) == 0){ // pion P() -> 0
                tmp = new int[2];
                tmp[0] = x+1;
                tmp[1] = y;
                arl.add(indexMove++,  tmp);
            }
            if(check_specified_pawn(x, y, x, y-1) == 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y-1;
                arl.add(indexMove++, tmp);
            }
            if(check_specified_pawn(x, y, x, y+1) == 0){
                tmp = new int[2];
                tmp[0] = x;
                tmp[1] = y+1;
                arl.add(indexMove++, tmp);
            }
            if(check_specified_pawn(x, y, x+1, y+1) == 0){
                tmp = new int[2];
                tmp[0] = x+1;
                tmp[1] = y+1;
                arl.add(indexMove++, tmp);
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
        if(indexJump + indexMove ==0 ){
            return null;
        }
        return retour;

    }

    /**
     * Permet d'obtenir la liste des possibilites de mouvement d'une fleche
     * @param p Fleche a verifier
     * @param x sa position en x
     * @param y sa position en y
     * @return liste de possibilites de mouvement
     */
    private int[][] get_possibilitiesArrow(Pion p, int x, int y){
        ArrayList<int[]> arl=new ArrayList<int[]>(); // moves
        ArrayList<int[]> ar2=new ArrayList<int[]>(); //jump
        int[] liste_int = new int[2];
        int[] tmp = new int[2];
        int indexMove = 0;
        int indexJump = 0;

        int possibleEnemyJump = 0; // est-ce que le pion peut jump un ennemi
        //cest un flag

        if (p.get_direction() == 0){
            //jumps


                if (check_specified_pawn(x, y, x - 1, y) > 0 && check_specified_pawn(x, y, x - 2, y) == 0) {// pion P() -> 0
                    if(!ifPawnJumped(x-1,y)) {
                        tmp = new int[2];
                        tmp[0] = x - 2;
                        tmp[1] = y;
                        //ar2.add(indexJump++, tmp);

                        if(check_specified_pawn(x,y,x-1,y) == 2){
                            possibleEnemyJump++;
                            if(has_jumped == (byte)0) {
                                if (possibleEnemyJump == 1) {
                                    ar2 = new ArrayList<int[]>();

                                }
                                ar2.add(ar2.size(), tmp);
                            }
                        }
                        if(possibleEnemyJump == 0 || has_jumped == (byte) 1) {
                            ar2.add(ar2.size(), tmp);
                        }
                    }
                }
                if (check_specified_pawn(x, y, x, y + 2) == 0 && check_specified_pawn(x, y, x, y + 1) > 0 && has_jumped == (byte) 1) {
                    if(!ifPawnJumped(x,y+1)) {
                        tmp = new int[2];
                        tmp[0] = x;
                        tmp[1] = y + 2;
                        ar2.add(ar2.size(), tmp);
                        if(check_specified_pawn(x,y,x,y+1) == 2){
                            possibleEnemyJump++;
                        }
                    }
                }
                if (check_specified_pawn(x, y, x, y - 2) == 0 && check_specified_pawn(x, y, x, y - 1) > 0 && has_jumped == (byte) 1) {
                    if(!ifPawnJumped(x,y-1)) {
                        tmp = new int[2];
                        tmp[0] = x;
                        tmp[1] = y - 2;
                        ar2.add(ar2.size(), tmp);
                        if(check_specified_pawn(x,y,x,y-1) == 2){
                            possibleEnemyJump++;
                        }
                    }
                }
                if (check_specified_pawn(x, y, x - 2, y - 2) == 0 && check_specified_pawn(x, y, x - 1, y - 1) > 0) {
                    if(!ifPawnJumped(x-1,y-1)) {
                        tmp = new int[2];
                        tmp[0] = x - 2;
                        tmp[1] = y - 2;
                        //ar2.add(indexJump++, tmp);
                        if(check_specified_pawn(x,y,x-1,y-1) == 2){
                            possibleEnemyJump++;
                            if(has_jumped == (byte)0) {
                                if (possibleEnemyJump == 1) {
                                    ar2 = new ArrayList<int[]>();

                                }
                                ar2.add(ar2.size(), tmp);
                            }
                        }
                        if(possibleEnemyJump == 0 || has_jumped == (byte) 1) {
                            ar2.add(ar2.size(), tmp);
                        }
                    }
                }
                if (possibleEnemyJump == 0) {
                    //move
                    if (check_specified_pawn(x, y, x - 1, y) == 0) { // pion P() -> 0
                        tmp = new int[2];
                        tmp[0] = x - 1;
                        tmp[1] = y;
                        arl.add(indexMove, tmp);
                        indexMove += 1;
                    }
                    if (check_specified_pawn(x, y, x - 1, y - 1) == 0) {
                        tmp = new int[2];
                        tmp[0] = x - 1;
                        tmp[1] = y - 1;
                        arl.add(indexMove, tmp);
                        indexMove += 1;

                    }

            }
        }

        else {
            //jumps
            if(check_specified_pawn(x, y, x+2, y) == 0  && check_specified_pawn(x, y, x+1, y) > 0){ // pion P() -> 0
                if(!ifPawnJumped(x+1,y)) {
                    tmp = new int[2];
                    tmp[0] = x + 2;
                    tmp[1] = y;
                    ar2.add(indexJump++, tmp);
                    if(check_specified_pawn(x,y,x+1,y) == 2){
                        possibleEnemyJump++;
                        if(has_jumped == (byte)0) {
                            if (possibleEnemyJump == 1) {
                                ar2 = new ArrayList<int[]>();

                            }
                            ar2.add(ar2.size(), tmp);
                        }
                    }
                    if(possibleEnemyJump == 0 || has_jumped == (byte) 1) {
                        ar2.add(ar2.size(), tmp);
                    }
                }
            }
            if(check_specified_pawn(x, y, x, y-2) == 0  && check_specified_pawn(x, y, x, y-1) > 0 && has_jumped == (byte)1){
                if(!ifPawnJumped(x,y-1)) {
                    tmp = new int[2];
                    tmp[0] = x;
                    tmp[1] = y-2;
                    ar2.add(ar2.size(), tmp);
                }
            }
            if(check_specified_pawn(x, y, x, y+2) == 0  && check_specified_pawn(x, y, x, y+1) > 0 && has_jumped == (byte)1){
                if(!ifPawnJumped(x,y+1)) {
                    tmp = new int[2];
                    tmp[0] = x;
                    tmp[1] = y + 2;
                    ar2.add(ar2.size(), tmp);

                }
            }
            if(check_specified_pawn(x, y, x+2, y+2) == 0  && check_specified_pawn(x, y, x+1, y+1) > 0){
                if(!ifPawnJumped(x+1,y+1)) {
                    tmp = new int[2];
                    tmp[0] = x + 2;
                    tmp[1] = y + 2;

                    if(check_specified_pawn(x,y,x+1,y+1) == 2){
                        possibleEnemyJump++;
                        if(has_jumped == (byte)0) {
                            if (possibleEnemyJump == 1) {
                                ar2 = new ArrayList<int[]>();

                            }
                            ar2.add(ar2.size(), tmp);
                        }
                    }
                    if(possibleEnemyJump == 0 || has_jumped == (byte) 1) {
                        ar2.add(ar2.size(), tmp);
                    }
                }
            }
            //move
            if(possibleEnemyJump == 0){
                if(check_specified_pawn(x, y, x+1, y) == 0){ // pion P() -> 0
                    tmp = new int[2];
                    tmp[0] = x+1;
                    tmp[1] = y;
                    arl.add(indexMove++,  tmp);
                }
                if(check_specified_pawn(x, y, x+1, y+1) == 0){
                    tmp = new int[2];
                    tmp[0] = x+1;
                    tmp[1] = y+1;
                    arl.add(indexMove++, tmp);
                }
            }
        }
        indexMove = arl.size();
        possible_move= arl.toArray(new int[0][0]);
        possible_jump = ar2.toArray(new int[0][0]);
        if(has_jumped == (byte) 1){
            indexMove = 0;
            possible_move=null;
        }
        indexJump = ar2.size();


        int[][] retour = new int[indexJump+indexMove][2];
        for(int i=0; i<indexJump+indexMove;i++){
            if(i<indexMove && indexMove != 0){
                retour[i] = possible_move[i];
            }
            if(i>=indexMove && indexJump !=0){
                retour[i] = possible_jump[i-indexMove];

            }
        }
        if(indexJump + indexMove ==0 ){
            return null;
        }
        return retour;
    }

    private boolean ifPawnJumped(int x,int y){
        if(jumpedPawn == null){
            return false;
        }
        for(int i= 0; i<jumpedPawn.size(); i++){
            if(jumpedPawn.get(i)[0][0] == x && jumpedPawn.get(i)[0][1]==y){
                return true;
            }
        }
        return false;
    }
    // permet de savoir lorsque qu'un pion peut jump un ennemi ou non

    /**
     * Verfie si une fleche peut sauter un ennemi
     * @param x position en X du pion
     * @param y position en Y du pion
     * @return vrai si il peut jump un ennemi 0 sinon
     */
    private boolean canJumpEnnemy(int x, int y){
        if(gameboard[x][y].get_direction() == 0) {
            // vers le haut
            if (check_specified_pawn(x, y, x - 2, y - 2) == 0) {
                if (check_specified_pawn(x, y, x - 1, y - 1) == 2) {
                    return true;
                }
            }
            if (check_specified_pawn(x, y, x - 2, y) == 0) {
                if (check_specified_pawn(x, y, x - 1, y) == 2) {
                    return true;
                }
            }
        }
        if(gameboard[x][y].get_direction() == 1){

            // vers le bas
            if(check_specified_pawn(x, y, x + 2, y + 2) == 0){
                if(check_specified_pawn(x, y, x + 1, y + 1 ) == 2){
                    //on verifie que ce n'est pas un rejump
                    if(!(x+2==lastPosition[0]&& y+2== lastPosition[1]))
                        return true;
                }
            }
            if(check_specified_pawn(x, y, x + 2, y ) == 0){
                if(check_specified_pawn(x, y, x + 1, y ) == 2){
                    //on verifie que ce n'est pas un rejump
                    if(!(x+2==lastPosition[0]&& y== lastPosition[1]))
                        return true;
                }
            }
        }
        return false;
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

    // fonction pour deplacer les pions
    // return -1 si il a pas bougé
    // return 0 si il  a bougé

    /**
     * Fonction de deplacement d'un pion (met a jour has_jumped, jump, selection et movedPawn)
     * @param x Position en x du pion
     * @param y Position en y du pion
     * @return Renvoi 1 ou 0 si il a bougé -1 sinon
     */
    public int move(int x, int y){

        if(check_specified_pawn(x, y,selection[0], selection[1]) == -1){
            //System.err.println("Tried to move a non-existing pawn");
            return -1;
        }
        // commence par s'il y a des jumps
        if(possible_jump != null){
            for(int i=0; i<possible_jump.length;i++){

                if (possible_jump[i][0]==x && possible_jump[i][1] == y) {
                    int distanceX = (x - selection[0]) / 2;
                    int distanceY = (y - selection[1]) / 2;
                    if (gameboard[selection[0]][selection[1]] instanceof Etoile) {
                        has_jumped = (byte) 0;
                        jump--;
                        if (jump <= -1) {
                            jump=0;
                            //System.err.println("Star has 0 turn left");
                            return -1;
                        }

                    } else {
                        has_jumped = (byte) 1;
                        if (gameboard[distanceX + selection[0]][distanceY + selection[1]].get_color() != gameboard[selection[0]][selection[1]].get_color()) {

                            jump++;
                        }
                        int[][] test = {{distanceX + selection[0],distanceY + selection[1]}};

                        jumpedPawn.add(jumpedPawn.size(),test);
                    }
                    movedPawn = new int[2];
                    movedPawn[0] = x;
                    movedPawn[1] = y;
                    lastPosition = new int[2];
                    lastPosition[0] = selection[0];
                    lastPosition[1] = selection[1];
                    gameboard[x][y] = gameboard[selection[0]][selection[1]];
                    gameboard[selection[0]][selection[1]] = new Pion();
                    possible_move = null;
                    if ((x == 0 && gameboard[x][y].get_direction() == 0) || (x == 6 && gameboard[x][y].get_direction() == 1)) {
                        if (gameboard[x][y] instanceof Etoile) {
                            if (gameboard[x][y].get_color() == (byte)0) {
                                nb_W_stars-=1;
                            } else if (gameboard[x][y].get_color() == (byte)1) {
                                nb_B_stars-=1;
                            }
                            gameboard[x][y] = new Pion();
                        } else if (gameboard[x][y] instanceof Fleche) {
                            gameboard[x][y].change_direction();
                        }
                    }
                    return 1;
                }
            }
        }
        if(possible_move != null){
            for(int i=0; i< possible_move.length;i++){
                if(possible_move[i][0] == x && possible_move[i][1] == y){
                    gameboard[x][y] = gameboard[selection[0]][selection[1]];
                    movedPawn = new int[2];
                    movedPawn[0] = x;
                    movedPawn[1] = y;
                    lastPosition = new int[2];
                    lastPosition[0] = selection[0];
                    lastPosition[1] = selection[1];
                    gameboard[selection[0]][selection[1]] = new Pion();
                    possible_jump = null;
                    possible_move = null;
                    if(gameboard[x][y] instanceof Etoile){
                        jump --;
                        has_jumped = (byte) 0;
                    }
                    if((x==0 && gameboard[x][y].get_direction()==0) || (x==6 && gameboard[x][y].get_direction()==1)){
                        if (gameboard[x][y] instanceof Etoile)
                        {
                            if (gameboard[x][y].get_color() == (byte)0) {
                                nb_W_stars-=1;
                            } else if (gameboard[x][y].get_color() == (byte)1) {
                                nb_B_stars-=1;
                            }

                            gameboard[x][y]=new Pion();
                        }
                        else if (gameboard[x][y] instanceof Fleche) {
                            gameboard[x][y].change_direction();
                        }
                    }
                    return 1;
                }

            }
            return 0;
        }
        //System.err.println("Destination not in possible move/jump");
        return -1;
    }

    /**
     * Verfie si c'est la fin du tour
     * @return vrai si c'est la fin du tour, au sinon
     */
    public boolean checkEndTurn(){
        // lorsque l'on est avec une fleche
        if(gameboard[movedPawn[0]][movedPawn[1]] instanceof  Fleche){
            // si il a fait un deplacement on arrete le tour
            if(has_jumped == (byte) 0){
                return true ;
            }
            int[][] tmp = get_possibilitiesArrow(gameboard[movedPawn[0]][movedPawn[1]], movedPawn[0], movedPawn[1]);
            if((jump < 1) && (tmp == null)){
                return true;
            }
        }
        // si on a bouge une etoile
        else{
            // on regarde le nombre de saut restant
            if(jump <= 0){
                return true;
            }
        }
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public boolean checkEndGame(){
        return (nb_W_stars == 0 || nb_B_stars == 0);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(turn);
        dest.writeInt(nb_W_stars);
        dest.writeInt(nb_B_stars);
        dest.writeByte(has_jumped);
        dest.writeInt(jump);
        dest.writeIntArray(selection);
        dest.writeIntArray(movedPawn);
        dest.writeIntArray(lastPosition);
    }

    /**
     * Permet de finir un tour et de savoir si c'est la fin de la partie
     * @return 1 si c'est la fin de la partie 0 sinon
     */
    public byte end_turn(){
        this.jump=0;
        possible_jump = null;
        possible_move = null;
        has_jumped = (byte) 0;
        selection = new int[2];
        movedPawn = new int[2];
        lastPosition = new int[2];
        jumpedPawn = new ArrayList<int[][]>();
        if (nb_B_stars == 0 || nb_W_stars == 0){
            return (byte)1;
        }
        turn++;
        return (byte)0;
    }

    /**
     * Ajoute un tour au tour de jeu
     */
    public void add_turn(){
        turn++;
    }

    // getters and setters
    public Context getContext() {
        return context;
    }

    public void set_movedPawn(int x, int y) {
        movedPawn[0] = x;
        movedPawn[1] = y;
    }

    @Override
    public String toString() {
        String board = "";

        // a -> null
        // b -> pion noir haut
        // c -> pion noir bas
        // d -> etoile noire
        // e -> pion blanc haut
        // f -> pion blanc bas
        // g -> etoile blanche
        // h -> case vide

        for(int i=0; i<gameboard.length;i++){
            for(int j=0;j<gameboard[i].length;j++){
                // Cell doesn't exists
                if(gameboard[i][j] == null){
                    board += 'a';
                }
                // No pawn in the cell
                else if(gameboard[i][j].get_color() == -1){
                    board += 'h';
                }
                // Star
                else if(gameboard[i][j] instanceof Etoile){
                    if(gameboard[i][j].get_color() == 1){
                        board += 'd';
                    }
                    else{
                        board += 'g';
                    }
                }
                // Arrow
                else{
                    // Black
                    if(gameboard[i][j].get_color() == 1){
                        // Up
                        if(gameboard[i][j].get_direction() == 0){
                            board += 'b';
                        }
                        // Down
                        else{
                            board += 'c';
                        }
                    }
                    // White
                    else{
                        // Up
                        if(gameboard[i][j].get_direction() == 0){
                            board += 'e';
                        }
                        // Down
                        else{
                            board += 'f';
                        }
                    }
                }
            }
        }

        String s = "";
        int count = 1;
        char tmp = board.charAt(0);

        for(int i = 0; i < 62; i ++){
            if(tmp == board.charAt(i + 1)){
                count ++;
                continue;
            }
            s += Integer.toString(count);
            s += tmp;
            tmp = board.charAt(i + 1);
            count = 1;
        }
        s += Integer.toString(count);
        s += tmp;
        // System.out.println(board);
        // System.out.println(count);
        return s;
    }

    public void setGameboard(String s){
        int n;
        char p;
        int x = 0;
        int y = 0;
        int count = 0;

        // a -> null
        // b -> pion noir haut
        // c -> pion noir bas
        // d -> etoile noire
        // e -> pion blanc haut
        // f -> pion blanc bas
        // g -> etoile blanche
        // h -> case vide

        // parcourt de la string
        for(int i = 0; i < s.length() - 1; i += 2){
            n = Character.getNumericValue(s.charAt(i));
            p = s.charAt(i +1);
            for(int j = 0; j < n ; j ++, count ++){

                if(y+1 == 9){
                    x ++;
                }
                y = count % 9;
                switch (p){
                    case 'a':
                        gameboard[x][y] = null;
                        break;
                    case 'b':
                        gameboard[x][y] = new Fleche((byte) 1, (byte) 0);
                        break;
                    case 'c':
                        gameboard[x][y] = new Fleche((byte) 1, (byte) 1);
                        break;
                    case 'd':
                        gameboard[x][y] = new Etoile((byte) 1, (byte) 1);
                        break;
                    case 'e':
                        gameboard[x][y] = new Fleche((byte) 0, (byte) 0);
                        break;
                    case 'f':
                        gameboard[x][y] = new Fleche((byte) 0, (byte) 1);
                        break;
                    case 'g':
                        gameboard[x][y] = new Etoile((byte) 0, (byte) 0);
                        break;
                    case 'h':
                        gameboard[x][y] = new Pion();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

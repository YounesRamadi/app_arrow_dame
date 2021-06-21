package controller;


import java.util.ArrayList;

public class GameBoard {
    private Pion[][] gameboard = new Pion[7][9];
    private int nb_W_stars;
    private int nb_B_stars;
    public int turn;

    private byte has_jumped;
    private int jump;

    private int[][] possible_jump;
    private  int[][] possible_move;

    private int[] selection = new int[2];
    private int[] movedPawn = new int[2];
    private int[] lastPosition = new int[2];

    public Pion[][] getGameboard() {
        return gameboard;
    }

    public GameBoard() {
        // initGameBoard();
        String entree = "03b03a07b25o07d03c03d";

        this.gameboard = makeGameBoard(entree);
        setNb_stars();
    }

    public GameBoard(String ch) {
        this.gameboard = makeGameBoard(ch);
        setNb_stars();
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
        if(p != null)
            gameboard[x][y] = p;
    }

    public int getNb_W_stars() {
        return nb_W_stars;
    }

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

    public int getJump(){
        return jump;
    }

    public void setJump(int jump){
        this.jump = jump;
    }

    public Pion[][] makeGameBoard(String ch){
        int[] tab = new int[63];
        int i=0;
        int oc = 0;
        int val=0;
        int j=0;
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

    public byte end_turn(){
        jump=0;
        possible_jump = null;
        possible_move = null;
        has_jumped = (byte) 0;
        selection = new int[2];
        movedPawn = new int[2];
        lastPosition = new int[2];
        if (nb_B_stars == 0 || nb_W_stars == 0){
            return (byte)1;
        }
        turn++;
        return (byte)0;
    }

    public Pion getCell(int x, int y) {
        return this.gameboard[x][y];
    }

    // check_selection permet de verifier que l'utilisateur puisse prendre la piece
    // honetement on sait pas ce qu'elle fait
    public int check_selection(int x, int y, int turn, int checkforjump) {
        
        if(x <0 || x >= 7 || y < 0 || y >= 9){
            System.err.println("Wtf args are you sending bruh ?");
            return -1;
        }

        if(gameboard[x][y].get_color() != turn%2){
            System.err.println("That's not your pawn!");
            return -1;
        }

        if(gameboard[x][y] instanceof Etoile){
            if(jump < 1){
                System.err.println("You can't move this star!");
                return -1;
            }
            return 0;
        }
        else{
            if(movedPawn != null){
                if(gameboard[movedPawn[0]][movedPawn[1]] instanceof Etoile) {
                    System.err.println("You can't move this arrow! ( you just moved a star )");
                    return -1;
                }
                else{
                    if(has_jumped == (byte) 0){
                        return 0;
                    }
                    else{
                        if((x == movedPawn[0]) && (y == movedPawn[1])){
                            return 0;
                        }
                        else{
                            System.err.println("That's not the arrow you just moved!");
                            return -1;
                        }
                    }
                }
            }
        }

        int actual_color = gameboard[x][y].get_color();

        if (checkforjump == 1){
            for(int i=0; i<7;i++){
                for(int j=0; j<9; j++){
                    if(gameboard[i][j].get_color() == actual_color){
                        if(!(i== x && j ==y)) {
                            get_possibilities(gameboard[i][j], i, j);
                            if (possible_jump != null && gameboard[x][y] instanceof Fleche) {
                                for (int k = 0; k < possible_jump.length; k++) {
                                    if (gameboard[possible_jump[k][0]][possible_jump[k][1]].get_color() != actual_color) {
                                        System.err.println("An other pawn can jump, impossible to move this arrow");
                                        return -1;
                                    }
                                }
                                possible_jump = null;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    // returnne 0 si cest vide
    // returnne 1 si cest la même color
    // returnne 2 si la color est differente
    // returnne -1 si le pion est en dehors de la grille
    public int check_specified_pawn(int posPionx, int posPiony, int posFinalx, int posFinaly){
        // toutes les positions
        if(posPionx < 0 || posPionx >= 7 || posPiony < 0 || posPiony >= 9){
            System.err.println("PositionX and Position Y of the target pawn is out of the board.");
            return -1;
        }
        if(posFinalx < 0 || posFinalx >= 7 || posFinaly < 0 || posFinaly >= 9){
            System.err.println("PositionX and Position Y of the checked pawn is out of the board.");
            return -1;
        }
        if(gameboard[posFinalx][posFinaly] ==null){
            System.err.println("Target pawn is out of the board but still in the array");
            return -1;
        }
        if(gameboard[posFinalx][posFinaly].get_color() == -1)
            return 0;
        if(gameboard[posPionx][posPiony].get_color() == gameboard[posFinalx][posFinaly].get_color())
            return 1;
        return 2;
        // check colors

    }

    public int[][] get_possibilities(Pion p, int x, int y){
        System.out.println("init"+ x+"/"+y);
        selection[0] = x;
        selection[1] = y;

        if(gameboard[x][y] instanceof Etoile){
            return get_possibilitiesStar(gameboard[x][y], x, y);
        }else if(gameboard[x][y] instanceof Fleche){
            return get_possibilitiesArrow(gameboard[x][y], x, y);
        }
        return null;

    }

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
            System.out.println("test:"+ x+y+lastPosition[0]+lastPosition[1]);


                if (check_specified_pawn(x, y, x - 1, y) > 0 && check_specified_pawn(x, y, x - 2, y) == 0) {// pion P() -> 0
                    if(!((x-2 == lastPosition[0]) && (y == lastPosition[1]))) {
                        tmp = new int[2];
                        tmp[0] = x - 2;
                        tmp[1] = y;
                        ar2.add(indexJump++, tmp);
                        if(check_specified_pawn(x,y,x-1,y) == 2){
                            possibleEnemyJump++;
                        }
                    }
                }
                if (check_specified_pawn(x, y, x, y + 2) == 0 && check_specified_pawn(x, y, x, y + 1) > 0 && has_jumped == (byte) 1) {
                    if(!((x == lastPosition[0]) && (y+2 == lastPosition[1]))) {
                        tmp = new int[2];
                        tmp[0] = x;
                        tmp[1] = y + 2;
                        ar2.add(indexJump++, tmp);
                        if(check_specified_pawn(x,y,x,y+1) == 2){
                            possibleEnemyJump++;
                        }
                    }
                }
                if (check_specified_pawn(x, y, x, y - 2) == 0 && check_specified_pawn(x, y, x, y - 1) > 0 && has_jumped == (byte) 1) {
                    if(!((x == lastPosition[0]) && (y-2 == lastPosition[1]))) {
                        tmp = new int[2];
                        tmp[0] = x;
                        tmp[1] = y - 2;
                        ar2.add(indexJump++, tmp);
                        if(check_specified_pawn(x,y,x,y-1) == 2){
                            possibleEnemyJump++;
                        }
                    }
                }
                if (check_specified_pawn(x, y, x - 2, y - 2) == 0 && check_specified_pawn(x, y, x - 1, y - 1) > 0) {
                    if(!((x-2 == lastPosition[0]) && (y-2 == lastPosition[1]))) {
                        tmp = new int[2];
                        tmp[0] = x - 2;
                        tmp[1] = y - 2;
                        ar2.add(indexJump++, tmp);
                        if(check_specified_pawn(x,y,x-1,y-1) == 2){
                            possibleEnemyJump++;
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
                if(!((x+2 == lastPosition[0]) && (y == lastPosition[1]))) {
                    tmp = new int[2];
                    tmp[0] = x + 2;
                    tmp[1] = y;
                    ar2.add(indexJump++, tmp);
                    if(check_specified_pawn(x,y,x+1,y) == 2){
                        possibleEnemyJump++;
                    }
                }
            }
            if(check_specified_pawn(x, y, x, y-2) == 0  && check_specified_pawn(x, y, x, y-1) > 0 && has_jumped == (byte)1){
                if(!((x == lastPosition[0]) && (y-2 == lastPosition[1]))) {
                    tmp = new int[2];
                    tmp[0] = x;
                    tmp[1] = y-2;
                    ar2.add(indexJump++, tmp);
                    if(check_specified_pawn(x,y,x,y-1) == 2){
                        possibleEnemyJump++;
                    }
                }
            }
            if(check_specified_pawn(x, y, x, y+2) == 0  && check_specified_pawn(x, y, x, y+1) > 0 && has_jumped == (byte)1){
                if(!((x == lastPosition[0]) && (y+2 == lastPosition[1]))) {
                    tmp = new int[2];
                    tmp[0] = x;
                    tmp[1] = y + 2;
                    ar2.add(indexJump++, tmp);
                    if(check_specified_pawn(x,y,x,y+1) == 2){
                        possibleEnemyJump++;
                    }
                }
            }
            if(check_specified_pawn(x, y, x+2, y+2) == 0  && check_specified_pawn(x, y, x+1, y+1) > 0){
                if(!((x+2 == lastPosition[0]) && (y+2 == lastPosition[1]))) {
                    tmp = new int[2];
                    tmp[0] = x + 2;
                    tmp[1] = y + 2;
                    ar2.add(indexJump++, tmp);
                    if(check_specified_pawn(x,y,x+1,y+1) == 2){
                        possibleEnemyJump++;
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

        if(has_jumped == (byte) 1){
            indexMove = 0;
        }
        System.out.println("index jump : " + indexJump);
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
    public int move(int x, int y){

        if(check_specified_pawn(x, y,selection[0], selection[1]) == -1){
            System.err.println("Tried to move a non-existing pawn");
            return -1;
        }
        // commence par s'il y a des jumps
        if(possible_jump != null){
            for(int i=0; i<possible_jump.length;i++){
                System.out.println("possible jump :" + possible_jump.length + possible_jump[i][0] + possible_jump[i][1]);

                if (possible_jump[i][0]==x && possible_jump[i][1] == y) {
                    int distanceX = (x - selection[0]) / 2;
                    int distanceY = (y - selection[1]) / 2;
                    System.out.println("test");
                    if (gameboard[selection[0]][selection[1]] instanceof Etoile) {
                        has_jumped = (byte) 0;
                        jump--;
                        if (jump <= -1) {
                            System.err.println("Star has 0 turn left");
                            return -1;
                        }

                    } else {
                        has_jumped = (byte) 1;
                        if (gameboard[distanceX + selection[0]][distanceY + selection[1]].get_color() != gameboard[selection[0]][selection[1]].get_color()) {
                            jump++;
                        }
                    }
                    movedPawn = new int[2];
                    movedPawn[0] = x;
                    movedPawn[1] = y;
                    lastPosition = new int[2];
                    lastPosition[0] = selection[0];
                    lastPosition[1] = selection[1];
                    System.out.println("dans move depuis:" + selection[0] + selection[1] + "vers :" + x + y);
                    gameboard[x][y] = gameboard[selection[0]][selection[1]];
                    gameboard[selection[0]][selection[1]] = new Pion();
                    possible_move = null;
                    System.out.println("zob");
                    if ((x == 0 && gameboard[x][y].get_direction() == 0) || (x == 6 && gameboard[x][y].get_direction() == 1)) {
                        if (gameboard[x][y] instanceof Etoile) {
                            System.out.println("zob");
                            if (gameboard[x][y].get_color() == (byte)0) {
                                System.out.println("zab");
                                nb_W_stars-=1;
                            } else if (gameboard[x][y].get_color() == (byte)1) {
                                System.out.println("zub");
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
        System.err.println("Destination not in possible move/jump");
        return -1;
    }

    public boolean checkEndTurn(){
        if(gameboard[movedPawn[0]][movedPawn[1]] instanceof  Fleche){
            System.out.println("fleche");
            if(has_jumped == (byte) 0){
                return true ;
            }
            int[][] tmp = get_possibilitiesArrow(gameboard[movedPawn[0]][movedPawn[1]], movedPawn[0], movedPawn[1]);
            if((jump < 1) && (tmp == null)){
                return true;
            }
        }
        else{
            if(jump < 1){
                return true;
            }
        }


        return false;
    }

}

package controller.ia;

import java.util.Random;

import controller.gameboard.GameBoard;
import controller.pawn.Pawn;
import controller.pawn.Star;


public class Ia {
    int jumpB = 0;
    int jumpW = 0;

    /**
     * calculate the score for the current game state for the IA
     *
     * @param j     the game state at the end of the algorithm
     * @param color the color of the last pawn wich has moved
     * @return the score of the current game state for the IA
     */
    public int valuation(GameBoard j, byte color) {
        int fEtoile = 3;
        int score = 5000;
        if (j.getNb_B_stars() == 0)
            return 10000000;
        if (j.getNb_B_stars() == 2)
            score += 20000;
        if (j.getNb_B_stars() == 1)
            score += 30000;
        if (j.getNb_B_stars() == 0)
            return 0;
        if (color == 1)
            score += jumpB * 90;
        if (color == 0) {
            if (jumpW >= 1)
                score -= jumpW * 70;
            if (jumpW >= 3)
                return 0;
        }
        jumpW = 0;
        jumpB = 0;
        //score+= j.getJump()*50000000;
        //System.out.println("A : "+j.getJump() + " B : " + j.getHas_jumped());

        for (int lig = 0; lig < 7; lig++) {
            for (int col = 0; col < 9; col++) {
                if (j.getGameboard()[lig][col] != null) {
                    j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                    if (j.getGameboard()[lig][col].getColor() == 1) {
                        if (lig == 0 && j.getGameboard()[lig][col].getColor() == 1 && !(j.getGameboard()[lig][col] instanceof Star))
                            score += 60;
                        if (lig > 0 && col > 0 && lig < 6 && col < 8) {
                            if (j.getGameboard()[lig - 1][col - 1] != null && j.getGameboard()[lig - 1][col] != null && j.getGameboard()[lig + 1][col] != null && j.getGameboard()[lig + 1][col + 1] != null && j.getGameboard()[lig + 1][col - 1] != null) {
                                if (j.getGameboard()[lig - 1][col - 1].getColor() == 1 && j.getGameboard()[lig - 1][col].getColor() == 1) {
                                    score += 20;
                                }
                                if (j.getGameboard()[lig + 1][col].getColor() == 1 && j.getGameboard()[lig + 1][col + 1].getColor() == 1) {
                                    score += 20;
                                }
                                if (j.getGameboard()[lig + 1][col].getColor() != -1 && j.getGameboard()[lig - 1][col].getColor() != -1) {
                                    score += 30;
                                }
                                if (j.getGameboard()[lig + 1][col - 1].getColor() != -1 && j.getGameboard()[lig - 1][col - 1].getColor() != -1) {
                                    score += 30;
                                }
                            }
                        }
                        if (j.getGameboard()[lig][col] instanceof Star)
                            score += (j.getPossibleMove().length - j.getPossibleJump().length) * 6;
                        else
                            score += (j.getPossibleMove().length - j.getPossibleJump().length) * 6;
                    } else {
                        if (j.getGameboard()[lig][col] instanceof Star)
                            score -= (j.getPossibleMove().length - j.getPossibleJump().length) * 7;
                        else
                            score -= (j.getPossibleMove().length - j.getPossibleJump().length) * 7;
                    }
                    //color 1 ceux qui sont haut
                    //color 0 ceux qui sont en bas
                    if (j.getGameboard()[lig][col].getDirection() != 1 && j.getGameboard()[lig][col].getColor() == 1)
                        score += 30;
                    if (lig + 2 <= 6 && col + 2 < 9)
                        if (j.getGameboard()[lig + 1][col] != null && j.getGameboard()[lig + 2][col] != null && j.getGameboard()[lig + 1][col + 1] != null && j.getGameboard()[lig + 2][col + 2] != null) {
                            if (j.getGameboard()[lig][col].getColor() == 1 && j.getGameboard()[lig][col] instanceof Star) {
                                score += lig * 85;
                                if ((j.getGameboard()[lig + 1][col].getColor() != -1 && j.getGameboard()[lig + 2][col].getColor() == -1) || (j.getGameboard()[lig + 1][col + 1].getColor() != -1 && j.getGameboard()[lig + 2][col + 2].getColor() == -1))
                                    score += 20;
                            }
                        }
                    if (j.getGameboard()[lig][col].getColor() == 0 && j.getGameboard()[lig][col] instanceof Star) {
                        score -= (6 - lig) * 84;
                    }
                }
            }
        }
        if (score <= 0) {
            score = 1;
        }
        return score;
    }

    /**
     * keep the best choice for the IA
     *
     * @param j         current gamestate
     * @param ligne     row of the last pawn moved
     * @param colonne   colomn of the last pawn moved
     * @param lim       the limite in depth of the algorithm
     * @param sameColor true if the pawn moved before is the same color
     * @param lim2      the second limit for the depth of the jump
     * @return the best choice for the IA
     */
    public int max(GameBoard j, int ligne, int colonne, int lim, boolean sameColor, int lim2) {
        if (!sameColor)
            j.setHasJumped((byte) 0);
        int lemove = j.move(ligne, colonne);
        int max = 0;
        int inter = 0;
        int obl = 0;
        int[][] poss;
        System.out.println("max jump color: " + ligne + "le move : " + colonne + "le flag : " + j.getJump() + "selected" + j.getSelection()[0] + "//" + j.getSelection()[1] + " Daronne de pierre ? " + (j.getGameboard()[ligne][colonne] instanceof Star) + "  " + (j.getGameboard()[ligne][colonne].getColor()));
        if (j.getHasJumped() == (byte) 1 && sameColor && lim2 > 0) {
            //System.out.println("Zebi:"+j.getJump());
            poss = j.getPossibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if (poss != null) {
                for (int i = 0; i < poss.length; i++) {
                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                    if (max < inter) {
                        max = inter;
                    }
                    inter = min(j.copy(), poss[i][0], poss[i][1], lim - 1, false, lim2 - 1);
                    if (max < inter) {
                        max = inter;
                    }
                }
            }
        }
        if (j.getJump() > 1 && sameColor && lim2 > 0) {
            jumpB = j.getJump();
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() == j.getGameboard()[ligne][colonne].getColor() && j.getGameboard()[lig][col] instanceof Star) {
                            poss = j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (poss != null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (j.getJump() == 1 && sameColor && lim2 > 0) {
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() == j.getGameboard()[ligne][colonne].getColor() && j.getGameboard()[lig][col] instanceof Star) {
                            poss = j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (poss != null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim - 1, true, lim2 - 1);

                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        } else if (j.getJump() <= 0) {
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() == j.getGameboard()[ligne][colonne].getColor() && j.getGameboard()[lig][col] instanceof Star) {

                            poss = j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (poss != null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim - 1, false, lim2 - 1);
                                    if (max < inter)
                                        max = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (lim <= 0 || lim2 <= 0 || j.getNb_W_stars() == 0 || j.getNb_B_stars() == 0) {
            //System.out.println("Ceci est un test");
            if (sameColor)
                return valuation(j, (byte) (1));
            else
                return valuation(j, (byte) (0));
        }
        if (j.getJump() == 0 && !sameColor) {
            j.setHasJumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() != j.getGameboard()[ligne][colonne].getColor() && j.getGameboard()[lig][col].getColor() != -1) {
                            j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (j.getPossibleJump() != null) {
                                for (int n = 0; n < j.getPossibleJump().length; n++) {
                                    if ((j.getGameboard()[lig + (j.getPossibleJump()[n][0] - lig) / 2][col + (j.getPossibleJump()[n][1] - col) / 2].getColor()) != j.getGameboard()[lig][col].getColor()) {
                                        if (obl == 0)
                                            max = 0;
                                        inter = max(j.copy(), j.getPossibleJump()[n][0], j.getPossibleJump()[n][1], lim, true, lim2 - 1);
                                        if (max < inter)
                                            max = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        inter = max(j.copy(), j.getPossibleJump()[n][0], j.getPossibleJump()[n][1], lim, true, lim2 - 1);
                                        if (max < inter)
                                            max = inter;
                                        inter = min(j.copy(), j.getPossibleJump()[n][0], j.getPossibleJump()[n][1], lim - 1, false, lim2 - 1);
                                        if (max < inter)
                                            max = inter;
                                    }
                                }
                            }
                        }

                        if (j.getPossibleMove() != null && obl == 0) {
                            for (int n = 0; n < j.getPossibleMove().length; n++) {
                                inter = min(j.copy(), j.getPossibleMove()[n][0], j.getPossibleMove()[n][0], lim - 1, false, lim2 - 1);
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
     *
     * @param j         current gamestate
     * @param ligne     row of the last pawn moved
     * @param colonne   colomn of the last pawn moved
     * @param lim       the limite in depth of the algorithm
     * @param sameColor true if the pawn moved before is the same color
     * @param lim2      the second limit for the depth of the jump
     * @return the badest choice for the IA
     */
    public int min(GameBoard j, int ligne, int colonne, int lim, boolean sameColor, int lim2) {
        if (!sameColor)
            j.setHasJumped((byte) 0);
        int lemove = j.move(ligne, colonne);
        System.out.println("lemove : " + j.getJump() + "\n");
        int min = 5000000;
        int inter = 0;
        int obl = 0;
        int[][] poss;
        //System.out.println("max jump color: " + ligne + "le move : " + colonne+ "le flag : "+ j.getJump() + "selected" +j.getSelection()[0]+"//"+j.getSelection()[1]+" Daronne de pierre ? "+ (j.getGameboard()[ligne][colonne] instanceof Fleche)+"  ");
        if (j.getHasJumped() == (byte) 1 && sameColor && lim2 > 0) {
            //System.out.println("Zebi:"+j.getJump());

            poss = j.getPossibilities(j.getGameboard()[ligne][colonne], ligne, colonne);
            if (poss != null) {
                for (int i = 0; i < poss.length; i++) {
                    inter = min(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                    if (min > inter) {
                        min = inter;
                    }
                    inter = max(j.copy(), poss[i][0], poss[i][1], lim - 1, false, lim2 - 1);
                    if (min > inter) {
                        min = inter;
                    }
                }
            }
        }
        if (j.getJump() > 1 && sameColor && lim2 > 0) {
            jumpW = j.getJump();
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() == j.getGameboard()[ligne][colonne].getColor() && j.getGameboard()[lig][col] instanceof Star) {
                            poss = j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (poss != null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                                    if (min > inter)
                                        min = inter;
                                }
                            }
                        }
                    }
                }
            }
        } else if (j.getJump() == 1 && sameColor && lim2 > 0) {
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() == j.getGameboard()[ligne][colonne].getColor() && j.getGameboard()[lig][col] instanceof Star) {
                            poss = j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (poss != null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim - 1, false, lim2 - 1);
                                    if (min > inter)
                                        min = inter;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (lim <= 0 || lim2 <= 0 || j.getNb_W_stars() == 0 || j.getNb_B_stars() == 0) {
            if (sameColor)
                return valuation(j, (byte) (0));
            else
                return valuation(j, (byte) (1));
        }
        if (j.getJump() <= 0 && !sameColor) {
            j.setHasJumped((byte) 0);
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() != j.getGameboard()[ligne][colonne].getColor() && j.getGameboard()[lig][col].getColor() != -1) {
                            j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (j.getPossibleJump() != null) {
                                for (int n = 0; n < j.getPossibleJump().length; n++) {
                                    if ((j.getGameboard()[lig + (j.getPossibleJump()[n][0] - lig) / 2][col + (j.getPossibleJump()[n][1] - col) / 2].getColor()) != j.getGameboard()[lig][col].getColor() && (j.getGameboard()[lig + (j.getPossibleJump()[n][0] - lig) / 2][col + (j.getPossibleJump()[n][1] - col) / 2].getColor()) != -1) {
                                        if (obl == 0)
                                            min = 5000000;
                                        inter = min(j.copy(), j.getPossibleJump()[n][0], j.getPossibleJump()[n][1], lim, true, lim2 - 1);
                                        if (min > inter)
                                            min = inter;
                                        obl = 1;
                                    } else if (obl == 0) {
                                        //System.out.println("la ligne "+ lig +" la colonne "+ col+" [0]: "+j.getPossible_jump()[n][0] +" ||  et [1]"+ j.getPossible_jump()[n][1]);
                                        inter = min(j.copy(), j.getPossibleJump()[n][0], j.getPossibleJump()[n][1], lim, true, lim2 - 1);
                                        if (min > inter)
                                            min = inter;
                                        inter = max(j.copy(), j.getPossibleJump()[n][0], j.getPossibleJump()[n][1], lim - 1, false, lim2 - 1);
                                        if (min > inter)
                                            min = inter;
                                    }

                                }
                            }
                        }
                        //System.out.println("Getpossiblemove : " + j.getPossible_move()[0] + " / " + j.getPossible_move()[1]);
                        if (j.getPossibleMove() != null && obl == 0) {
                            //System.out.println("le nombre de move possible d'après pierre :" + j.getPossible_move().length+"\n");
                            for (int n = 0; n < j.getPossibleMove().length; n++) {
                                //System.out.println(inter+"\n");
                                inter = max(j.copy(), j.getPossibleMove()[n][0], j.getPossibleMove()[n][0], lim - 1, false, lim2 - 1);
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
     *
     * @param couleur color of the Ia in game
     * @param j       current state of the game
     * @param lim     limit of depth
     * @return the best score against the best player
     */
    public int[] minMax(byte couleur, GameBoard j, int lim) {
        //System.out.println("fonction : " + j.getJump()+"\n");
        int lim2 = 6;
        Pawn[][] plateau = j.getGameboard();
        int[] pos = {-1, -1, -1, -1, 0, 0};
        int[][] possible_jump = {{-1, -1}};
        int[][] possible_move = {{-1, -1}};
        int[][] poss;
        Random rand = new Random();
        int obl = 0;
        int max = 0;
        int inter = 0;
        if (j.getJump() > 1) {
            if (j.getHasJumped() == (byte) 1) {
                poss = j.getPossibilities(j.getGameboard()[j.getMovedPawn()[0]][j.getMovedPawn()[1]], j.getMovedPawn()[0], j.getMovedPawn()[1]);
                if (poss != null) {
                    for (int i = 0; i < poss.length; i++) {
                        inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                        if (max < inter) {
                            max = inter;
                            pos[0] = j.getMovedPawn()[0];
                            pos[1] = j.getMovedPawn()[1];
                            pos[2] = poss[i][0];
                            pos[3] = poss[i][1];
                            possible_move = j.getPossibleMove();
                            possible_jump = j.getPossibleJump();
                        }
                    }
                }
            }
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() == couleur && j.getGameboard()[lig][col] instanceof Star) {
                            poss = j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (poss != null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                                    if (max < inter) {
                                        max = inter;
                                        pos[0] = lig;
                                        pos[1] = col;
                                        pos[2] = poss[i][0];
                                        pos[3] = poss[i][1];
                                        possible_move = j.getPossibleMove();
                                        possible_jump = j.getPossibleJump();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (j.getJump() == 1) {
            if (j.getHasJumped() == (byte) 1) {
                poss = j.getPossibilities(j.getGameboard()[j.getMovedPawn()[0]][j.getMovedPawn()[1]], j.getMovedPawn()[0], j.getMovedPawn()[1]);
                if (poss != null) {
                    for (int i = 0; i < poss.length; i++) {
                        inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                        if (max < inter) {
                            max = inter;
                            pos[0] = j.getMovedPawn()[0];
                            pos[1] = j.getMovedPawn()[1];
                            pos[2] = poss[i][0];
                            pos[3] = poss[i][1];
                            possible_move = j.getPossibleMove();
                            possible_jump = j.getPossibleJump();
                        }
                    }
                }
            }
            for (int lig = 0; lig < 7; lig++) {
                for (int col = 0; col < 9; col++) {
                    if (j.getGameboard()[lig][col] != null) {
                        if (j.getGameboard()[lig][col].getColor() == couleur && j.getGameboard()[lig][col] instanceof Star) {
                            poss = j.getPossibilities(j.getGameboard()[lig][col], lig, col);
                            if (poss != null) {
                                for (int i = 0; i < poss.length; i++) {
                                    inter = min(j.copy(), poss[i][0], poss[i][1], lim - 1, false, lim2 - 1);
                                    if (max < inter) {
                                        max = inter;
                                        pos[0] = lig;
                                        pos[1] = col;
                                        pos[2] = poss[i][0];
                                        pos[3] = poss[i][1];
                                        possible_move = j.getPossibleMove();
                                        possible_jump = j.getPossibleJump();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (j.getHasJumped() == (byte) 1) {
            poss = j.getPossibilities(j.getGameboard()[j.getMovedPawn()[0]][j.getMovedPawn()[1]], j.getMovedPawn()[0], j.getMovedPawn()[1]);
            if (poss != null) {
                for (int i = 0; i < poss.length; i++) {
                    inter = max(j.copy(), poss[i][0], poss[i][1], lim, true, lim2 - 1);
                    if (max < inter) {
                        max = inter;
                        pos[0] = j.getMovedPawn()[0];
                        pos[1] = j.getMovedPawn()[1];
                        pos[2] = poss[i][0];
                        pos[3] = poss[i][1];
                        possible_move = j.getPossibleMove();
                        possible_jump = j.getPossibleJump();
                    }
                }
            }

        } else {
            for (int ligne = 0; ligne < 7; ligne++) {
                for (int col = 0; col < 9; col++) {
                    if (plateau[ligne][col] != null && !(plateau[ligne][col] instanceof Star)) {
                        if (plateau[ligne][col].getColor() == couleur) {
                            j.getPossibilities(plateau[ligne][col], ligne, col);
                            if (j.getPossibleMove() != null) {
                                for (int s = 0; s < j.getPossibleMove().length; s++) {
                                    inter = min(j.copy(), j.getPossibleMove()[s][0], j.getPossibleMove()[s][1], lim - 1, false, lim2 - 1);
                                    inter += rand.nextInt(10);
                                    if (max < inter) {
                                        max = inter;
                                        pos[0] = ligne;
                                        pos[1] = col;
                                        pos[2] = j.getPossibleMove()[s][0];
                                        pos[3] = j.getPossibleMove()[s][1];
                                        possible_jump = j.getPossibleJump();
                                        possible_move = j.getPossibleMove();
                                    }
                                }
                                if (j.getPossibleJump() != null) {
                                    for (int s = 0; s < j.getPossibleJump().length; s++) {
                                        if ((j.getGameboard()[ligne + (j.getPossibleJump()[s][0] - ligne) / 2][col + (j.getPossibleJump()[s][1] - col) / 2].getColor()) != j.getGameboard()[ligne][col].getColor()) {
                                            if (obl == 0)
                                                max = 0;
                                            inter = max(j.copy(), j.getPossibleJump()[s][0], j.getPossibleJump()[s][1], lim, true, lim2 - 1);
                                            inter += rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossibleJump()[s][0];
                                                pos[3] = j.getPossibleJump()[s][1];
                                                possible_jump = j.getPossibleJump();
                                                possible_move = j.getPossibleMove();
                                            }
                                            inter = min(j.copy(), j.getPossibleJump()[s][0], j.getPossibleJump()[s][1], lim - 1, false, lim2 - 1);
                                            inter += rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossibleJump()[s][0];
                                                pos[3] = j.getPossibleJump()[s][1];
                                                possible_jump = j.getPossibleJump();
                                                possible_move = j.getPossibleMove();
                                            }
                                            obl = 1;
                                        } else if (obl == 0) {
                                            inter = max(j.copy(), j.getPossibleJump()[s][0], j.getPossibleJump()[s][1], lim, true, lim2 - 1);
                                            inter += rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossibleJump()[s][0];
                                                pos[3] = j.getPossibleJump()[s][1];
                                                possible_jump = j.getPossibleJump();
                                                possible_move = j.getPossibleMove();
                                            }
                                            inter = min(j.copy(), j.getPossibleJump()[s][0], j.getPossibleJump()[s][1], lim - 1, false, lim2 - 1);
                                            inter += rand.nextInt(10);
                                            if (max < inter) {
                                                max = inter;
                                                pos[0] = ligne;
                                                pos[1] = col;
                                                pos[2] = j.getPossibleJump()[s][0];
                                                pos[3] = j.getPossibleJump()[s][1];
                                                possible_jump = j.getPossibleJump();
                                                possible_move = j.getPossibleMove();
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
        j.setPossibleMove(possible_move);
        j.setPossibleJump(possible_jump);
        j.setSelection(pos[0], pos[1]);

        System.out.println("Mverenvoi : " + j.move(pos[2], pos[3]) + " pos[0]/ pos1 :" + pos[0] + pos[1] + " va en " + pos[2] + pos[3]);
        pos[4] = j.getHasJumped();
        pos[5] = j.getJump();
        return pos;
    }
}
package activities.game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import controller.ia.Ia;

import controller.gameboard.GameBoard;
import controller.pawn.Pawn;


public class DisplayBoardIaActivity extends AppCompatActivity {

    private GameBoard game;
    private LinearLayout layout;
    private RelativeLayout boardLayout;
    private RelativeLayout possibilitiesLayout;
    private Pawn[][] display_mat = new Pawn[7][9];
    private int[] selected = new int[2];
    private int sx;
    private int sy;
    private TextView nb_jump_w;
    private int turn = 0;
    public static final String BUNDLE_STATE_TURN="currentTurn";
    public static final String BUNDLE_STATE_GAMEBOARD="currentGameboard";
    private ImageView whiteScore;
    private ImageView blackScore;
    private Button endTurnBtn;
    private Toast toast;
    private String[] iaMoves;
    private int iaMovesIndex;
    private int[] iaMove;
    private Ia ia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            turn = savedInstanceState.getInt(BUNDLE_STATE_TURN);
            game = savedInstanceState.getParcelable(BUNDLE_STATE_GAMEBOARD);
        }
        else {
            turn = 0;
            game = new GameBoard(getApplicationContext());
        }

        iaMoves = new String[15];
        iaMove = new int[4];
        ia = new Ia();

        setContentView(R.layout.activity_display_board_ia);

        // initialisation of the needed layouts
        this.layout = (LinearLayout) findViewById(R.id.layout);
        this.boardLayout = (RelativeLayout) findViewById(R.id.board);
        this.possibilitiesLayout = (RelativeLayout) findViewById(R.id.possibilites);

        // initialisation of the needed views
        nb_jump_w = (TextView) findViewById(R.id.nb_jump_w);
        whiteScore = (ImageView) findViewById(R.id.scoreW);
        blackScore = (ImageView) findViewById(R.id.scoreB);

        endTurnBtn = new Button(this);
        endTurnBtn = (Button) findViewById(R.id.endTurn);
        endTurnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((game.getHasJumped() == (byte)1) && (game.getJump() <1)){
                    // Debug
                    // System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                    // End the player's turn
                    game.end_turn();
                    turn++;

                    runIa();

                    if(game.checkEndGame()){
                        game = new GameBoard(getApplicationContext());
                        // Debug
                        // System.out.println("Fin du game");
                    }

                    // End the ia's turn
                    game.end_turn();
                    turn ++;
                    updateDisplay();
                }
                else{
                    toast = Toast.makeText(getApplicationContext(), "You can't pass your turn", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        updateDisplay();
    }

    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt(BUNDLE_STATE_TURN, turn);
        outstate.putParcelable(BUNDLE_STATE_GAMEBOARD, game);
    }

    /**
     * Setter for the selected position
     *
     * @param a
     * @param b
     */
    public void setSelected(int a, int b) {
        this.selected[0] = a;
        this.selected[1] = b;
    }

    /**
     * Getter for the postion in the displayed matrix
     *
     * @return position in the displayed matrix
     */
    public int[] getSelected() {
        selected[0] = sx;

        if (sx == 0) {
            selected[1] = (sy + 7) % 9;
        } else if (selected[0] == 1) {
            selected[1] = (sy + 8) % 9;
        } else if (selected[0] == 2) {
            selected[1] = (sy + 8) % 9;
        } else if (selected[0] == 5) {
            selected[1] = (sy + 1) % 9;
        } else if (selected[0] == 6) {
            selected[1] = (sy + 1) % 9;
        } else {
            selected[1] = sy;
        }
        return selected;
    }

    /**
     * Update the board display
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateDisplay() {
        updateScores();
        updateTurn();
        // Cleaning the layout
        removeImages(boardLayout);

        // Getting the display matrix
        this.display_mat = game.display();

        int y = 0;
        int x = 0;
        for (int i = 0; i < 7; i++) {

            for (int j = 0; j < 9; j++) {

                // if the cell doesn't exist
                if (display_mat[i][j] == null) {
                    x += 100;
                    continue;
                }

                // Creation of the ImageView needed to print the Cell
                ImageView img = new ImageView(this);
                // Setting the Cell image
                img.setImageDrawable(getDrawable(display_mat[i][j].getImg()));

                int finalI = i;
                int finalJ = j;
                // make the Cell clickable only if it contains a pawn that belongs to the player
                if (display_mat[i][j].getColor() != -1) {
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Clean the possibilites layout
                            removeImages(possibilitiesLayout);

                            sx = finalI;
                            sy = finalJ;
                            setSelected(sx, sy);

                            // display the accessible cells if the pawn is able to move
                            if (game.checkSelection(getSelected()[0], getSelected()[1], turn, 1) == 0) {
                                display_possibilities(getSelected()[0], getSelected()[1]);
                            }
                            else if (game.checkSelection(getSelected()[0], getSelected()[1], turn, 1) == 1) {
                                display_possibilities(game.getMustJump()[0], game.getMustJump()[1]);
                            }
                            updateDisplay();
                        }
                    });
                }
                // Set the direction of the image depending on the pawn's direction
                if (display_mat[i][j].getDirection() == 0)
                    img.setRotation(270);
                else
                    img.setRotation(90);

                // Set the ImageView parameters and add it to the layout
                RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(100, 100);
                parms.setMargins(x - 50, y, 0, 0);
                parms.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                parms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                img.setLayoutParams(parms);

                boardLayout.addView(img);

                x += 100;

                // Debug
                getSelected();
            }
            y += 100;
            x = 50 * ((i + 1) % 2);
        }
        // Display the number of jumps available
        if((turn %2) == 0){
            nb_jump_w.setText(String.valueOf(game.getJump()));
        }
    }

    /**
     * Display the accessible Cells of a specific pawn on the gameboard
     *
     * @param px x position
     * @param py y position
     */

    @SuppressLint("UseCompatLoadingForDrawables")
    public void display_possibilities(int px, int py) {
        // Clean the posssibilities layout
        possibilitiesLayout.removeAllViews();

        // Check the existence of the specified pawn
        if (game.getGameboard()[px][py] != null && game.getGameboard()[px][py].getColor() != -1) {
            int[][] pos = null;

            if(game.checkSelection(px, py, turn, 1) != -1) {
                pos = game.getPossibilities(game.getGameboard()[px][py], px, py);
            }
            // Check the existence of the possibilities
            if(pos != null) {
                // for each possibilities
                for (int[] p : pos
                ) {
                    int[] tmp = getrelative_position(p);

                    int y = 0;
                    int x = 0;
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (i == tmp[0] && j == tmp[1]) {

                                // Creation of the ImageView needed to print the Cell
                                ImageView img = new ImageView(this);
                                img.setImageDrawable(getDrawable(R.drawable.hexagone_white));

                                int finalI = p[0];
                                int finalJ = p[1];

                                // Set the onClickListener
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateDisplay();
                                        setSelected(finalI, finalJ);
                                        // Debug
                                        // System.out.println("move depuis:" + getSelected()[0] + getSelected()[1] + "vers :" + finalI + finalJ);

                                        // move the pawn
                                        game.move(finalI, finalJ);

                                        // Check the end of the game or turn
                                        if (game.checkEndTurn() || game.checkEndGame()) {
                                            // Debug
                                            // System.out.println("fin de tour");

                                            // End of the player's
                                            turn ++;
                                            game.end_turn();

                                            if(game.checkEndGame()){
                                                game = new GameBoard(getApplicationContext());
                                                // Debug
                                                // System.out.println("Fin du game");
                                            }

                                            runIa();

                                            // End of the ia's turn
                                            turn ++;
                                            game.end_turn();

                                            if(game.checkEndGame()){
                                                game = new GameBoard(getApplicationContext());
                                                // Debug
                                                // System.out.println("Fin du game");
                                            }
                                        }
                                        removeImages(possibilitiesLayout);
                                        updateDisplay();
                                    }
                                });

                                // Set the ImageView parameters and add it to the layout
                                RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(100, 100);
                                parms.setMargins(x - 50, y, 0, 0);
                                parms.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                parms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                img.setLayoutParams(parms);
                                possibilitiesLayout.addView(img);
                            }
                            x += 100;
                        }
                        y += 100;
                        x = 50 * ((i+1) % 2);
                    }
                }
            }
        }
    }

    /**
     * Getter for the relative position of a displayed position
     *
     * @param pos position in the displayed matrix
     * @return position in the relative matrix
     */
    public int[] getrelative_position(int[] pos) {
        int[] new_pos = new int[2];
        new_pos[0] = pos[0];

        if (new_pos[0] == 0) {
            new_pos[1] = (pos[1] + 2) % 9;
        } else if (new_pos[0] == 1) {
            new_pos[1] = (pos[1] + 1) % 9;
        } else if (new_pos[0] == 2) {
            new_pos[1] = (pos[1] + 1) % 9;
        } else if (new_pos[0] == 5) {
            new_pos[1] = (pos[1] - 1) % 9;
        } else if (new_pos[0] == 6) {
            new_pos[1] = (pos[1] + 8) % 9;
        } else {
            new_pos[1] = pos[1];
        }
        return new_pos;
    }

    /**
     * Removes all chil view from a layout
     * Created because Layout.removeAllViews doesn't work well
     *
     * @param layout
     */
    public void removeImages(RelativeLayout layout) {
        for (int k = 0; k < 10; k++) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View view = layout.getChildAt(i);
                if (view.getId() != R.id.possibilites) {
                    layout.removeViewAt(i);
                }
            }
        }
    }

    /**
     * Used to update the score display
     */
    public void updateScores(){
        switch (game.getNb_W_stars()) {
            case 3:
                whiteScore.setImageDrawable(getDrawable(R.drawable.point_empty));
                break;
            case 2:
                whiteScore.setImageDrawable(getDrawable(R.drawable.point_blue_1));
                break;
            case 1:
                whiteScore.setImageDrawable(getDrawable(R.drawable.point_blue_2));
                break;
            case 0:
                whiteScore.setImageDrawable(getDrawable(R.drawable.point_blue_3));
                break;
            default:
                break;
        }
        switch (game.getNb_B_stars()) {
            case 3:
                blackScore.setImageDrawable(getDrawable(R.drawable.point_empty));
                break;
            case 2:
                blackScore.setImageDrawable(getDrawable(R.drawable.point_red_1));
                break;
            case 1:
                blackScore.setImageDrawable(getDrawable(R.drawable.point_red_2));
                break;
            case 0:
                blackScore.setImageDrawable(getDrawable(R.drawable.point_red_3));
                break;
            default:
                break;
        }
    }

    /**
     *  Used to update the border color (depending on the player turn
     */

    public void updateTurn(){
        if(turn%2 == 0){
            layout.setBackground(getDrawable(R.drawable.white_border));
        }
        else{
            layout.setBackground(getDrawable(R.drawable.black_border));
        }
    }

    /**
     *  Used to make the ia play
     */
    public void runIa() {
        do {
            updateTurn();
            iaMove = ia.minMax((byte) (1), game.copy(), 2);
            // System.out.println("Taking " + iaMove[0] + " : " + iaMove[1]);
            // System.out.println("Going in " + iaMove[2] + " : " + iaMove[3]);

            game.setSelection(iaMove[0], iaMove[1]);
            //jump = 0
            int[][] setterjump = new int[1][2];
            setterjump[0][0] = iaMove[2];
            setterjump[0][1] = iaMove[3];

            int[][] settermove = new int[1][2];
            settermove[0][0] = iaMove[2];
            settermove[0][1] = iaMove[3];

            game.setHasJumped((byte) iaMove[4]);
            //System.out.println("quil est bete : " + iaMove[5]);

            game.setPossibleJump(setterjump);
            game.setPossibleMove(settermove);

            if (iaMove[2] == -1 || iaMove[1] == -1) {
                break;
            }
            System.out.println("Moving : " + game.move(iaMove[2], iaMove[3]));
            game.setMovedPawn(iaMove[2], iaMove[3]);
            // System.out.println("Flags h_j :" + iaMove[4] + "/ j :" + iaMove[5]);

            // newIaMove();

            if (game.checkEndTurn()) {
                game.end_turn();
                break;
            }
            if (game.checkEndGame()) {
                game = new GameBoard(getApplicationContext());
                // System.out.println("Fin du game");
                break;
            }
        }while(iaMove[5] >= 1 || iaMove[4] == (byte)1);
    }


    /*
    public void initIaMoves(){
        for(int i = 0; i < 15; i++){
            iaMoves[i] = "";
        }
        iaMovesIndex = 0;
    }
    public void newIaMove(){
        iaMoves[iaMovesIndex] = game.toString();
        iaMovesIndex ++;
    }
    public void displayIaMoves()  {
        Handler handler = new Handler();
        layout.setBackground(getDrawable(R.drawable.black_border));
        for(int i = 0; i < iaMovesIndex; i++){
            System.out.println(iaMoves[i]);
            game.setGameboard(iaMoves[i]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){

            }

            update();
        }
        initIaMoves();
    }
    */
}
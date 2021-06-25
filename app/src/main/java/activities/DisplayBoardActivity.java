package activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import controller.GameBoard;
import controller.Pion;


public class DisplayBoardActivity extends AppCompatActivity {

    public static final String BUNDLE_STATE_TURN = "currentTurn";
    public static final String BUNDLE_STATE_GAMEBOARD = "currentGameboard";
    private final int[] selected = new int[2];
    private GameBoard game;
    private RelativeLayout boardLayout;
    private RelativeLayout possibilitiesLayout;
    private LinearLayout layout;
    private Pion[][] display_mat = new Pion[7][9];
    private int sx;
    private int sy;
    private Button btn;
    private TextView player;
    private TextView nb_jump_w;
    private TextView nb_jump_b;
    private Button endTurnButton;
    private Button endTurnButton1;
    private ImageView whiteScore;
    private ImageView blackScore;


    private int turn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            turn = savedInstanceState.getInt(BUNDLE_STATE_TURN);
            game = savedInstanceState.getParcelable(BUNDLE_STATE_GAMEBOARD);
        } else {
            turn = 0;
            game = new GameBoard(getApplicationContext());
        }

        setContentView(R.layout.activity_display_board);

        // initialisation of the needed layouts
        this.layout = findViewById(R.id.layout);
        this.boardLayout = findViewById(R.id.board);
        this.possibilitiesLayout = findViewById(R.id.possibilites);

        // initialisation of the needed views
        nb_jump_w = findViewById(R.id.nb_jump_w);
        nb_jump_b = findViewById(R.id.nb_jump_b);
        endTurnButton = findViewById(R.id.endTurn);
        endTurnButton1 = findViewById(R.id.endTurn1);
        whiteScore = findViewById(R.id.scoreW);
        blackScore = findViewById(R.id.scoreB);

        // initialisation of the needed onClickListeners
        endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                game.end_turn();
                turn++;
                update();
            }
        });

        endTurnButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                game.end_turn();
                turn++;
                update();
            }
        });

        update();
    }

    @Override
    protected void onSaveInstanceState(Bundle outstate) {
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
    public void update() {
        updateScores();
        updateTurn();
        // Cleaning the layout
        removeImages(boardLayout);

        // Debug
        System.out.println("turn:" + turn);
        System.out.println("gameboard toString : " + game.toString());

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
                if (display_mat[i][j].get_color() != -1) {
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Clean the possibilites layout
                            removeImages(possibilitiesLayout);

                            sx = finalI;
                            sy = finalJ;
                            setSelected(sx, sy);

                            // Debug
                            System.out.println(sx + " " + sy + "0" + getSelected()[0] + " " + getSelected()[1]);

                            // print the accessible cells if the pawn is able to move
                            if (game.check_selection(getSelected()[0], getSelected()[1], turn, 1) == 0) {
                                display_possibilities(getSelected()[0], getSelected()[1]);
                            }

                            // update display
                            update();
                        }
                    });
                }

                // Set the direction of the image depending on the pawn's direction
                if (display_mat[i][j].get_direction() == 0)
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

        // Print the number of jumps available
        if ((turn % 2) == 0) {
            nb_jump_b.setText("0");
            nb_jump_w.setText(String.valueOf(game.getJump()));
        } else if ((turn % 2) == 1) {
            nb_jump_w.setText("0");
            nb_jump_b.setText(String.valueOf(game.getJump()));
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
        removeImages(possibilitiesLayout);

        // Check the existence of the specified pawn
        if (game.getGameboard()[px][py] != null && game.getGameboard()[px][py].get_color() != -1) {
            int[][] pos = null;

            if (game.check_selection(px, py, turn, 1) != -1) {
                pos = game.get_possibilities(game.getGameboard()[px][py], px, py);
            }
            // Check the existence of the possibilities
            if (pos != null) {
                // for each possibilities
                for (int[] p : pos
                ) {

                    int[] tmp = getRelativePosition(p);

                    int y = 0;
                    int x = 0;
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 9; j++) {

                            if (i == tmp[0] && j == tmp[1]) {
                                // Debug
                                System.out.println(i + " " + j);

                                // Creation of the ImageView needed to print the Cell
                                ImageView img = new ImageView(this);
                                img.setImageDrawable(getDrawable(R.drawable.hexagone_white));

                                int finalI = p[0];
                                int finalJ = p[1];

                                // Set the onClickListener
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setSelected(finalI, finalJ);

                                        // Debug
                                        System.out.println("move depuis:" + getSelected()[0] + getSelected()[1] + "vers :" + finalI + finalJ);

                                        game.move(finalI, finalJ);

                                        // Check the end of the game or turn
                                        if (game.checkEndTurn() || game.checkEndGame()) {
                                            // Debug
                                            System.out.println("fin de tour");

                                            turn++;

                                            //Debug
                                            System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                                            game.end_turn();

                                            if (game.checkEndGame()) {
                                                System.out.println("Test");
                                                game = new GameBoard(getApplicationContext());
                                            }
                                            update();
                                        }
                                        removeImages(possibilitiesLayout);
                                        update();
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
                        x = 50 * ((i + 1) % 2);
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
    public int[] getRelativePosition(int[] pos) {
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

    public void updateTurn(){
        if(turn%2 == 0){
            layout.setBackground(getDrawable(R.drawable.white_border));
        }
        else{
            layout.setBackground(getDrawable(R.drawable.black_border));
        }
    }
}
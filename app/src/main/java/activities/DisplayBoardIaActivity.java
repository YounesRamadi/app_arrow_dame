package activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import ia.Ia;

import controller.GameBoard;
import controller.Pion;


public class DisplayBoardIaActivity extends AppCompatActivity {

    private GameBoard game;
    private LinearLayout layout;
    private RelativeLayout boardLayout;
    private RelativeLayout possibilitiesLayout;
    private Pion[][] display_mat = new Pion[7][9];
    private int[] selected = new int[2];
    private int sx;
    private int sy;
    private TextView nb_jump_w;
    private int turn = 0;
    public static final String BUNDLE_STATE_TURN="currentTurn";
    public static final String BUNDLE_STATE_GAMEBOARD="currentGameboard";
    private ImageView whiteScore;
    private ImageView blackScore;
    private Toast toast;
    private String[] iaMoves = new String[15];
    private int iaMovesIndex;



    private int[]  iaMove = new int[4];
    private Ia ia = new Ia();

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

        setContentView(R.layout.activity_display_board_ia);

        this.layout = findViewById(R.id.layout);
        this.boardLayout = (RelativeLayout) findViewById(R.id.board);
        this.possibilitiesLayout = (RelativeLayout) findViewById(R.id.possibilites);

        nb_jump_w = (TextView) findViewById(R.id.nb_jump_w);
        whiteScore = findViewById(R.id.scoreW);
        blackScore = findViewById(R.id.scoreB);

        // initIaMoves();

        Button endTurnBtn = new Button(this);
        endTurnBtn = (Button) findViewById(R.id.endTurn);
        endTurnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((game.getHas_jumped() == (byte)1) && (game.getJump() <1)){
                    // System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());
                    game.end_turn();
                    turn++;
                    runIa();
                    game.end_turn();

                    if(game.checkEndGame()){
                        game = new GameBoard(getApplicationContext());
                        System.out.println("Fin du game");
                    }

                    update();
                }
                else{
                    toast = Toast.makeText(getApplicationContext(), "You can't pass your turn", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        update();
    }

    @Override
    protected void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        outstate.putInt(BUNDLE_STATE_TURN, turn);
        outstate.putParcelable(BUNDLE_STATE_GAMEBOARD, game);
    }

    public void setSelected(int a, int b) {
        this.selected[0] = a;
        this.selected[1] = b;
    }

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

    @SuppressLint("UseCompatLoadingForDrawables")
    public void update() {
        updateScores();
        updateTurn();
        //System.out.println("turn:" + turn);
        // faudrait peut etre trouver autre chose
        removeImages(boardLayout);
        removeImages(boardLayout);
        removeImages(boardLayout);
        removeImages(boardLayout);
        removeImages(boardLayout);
        removeImages(boardLayout);
        removeImages(boardLayout);

        this.display_mat = game.display();

        int y = 0;
        int x = 0;
        for (int i = 0; i < 7; i++) {

            for (int j = 0; j < 9; j++) {
                if (display_mat[i][j] == null) {
                    x += 100;
                    continue;
                }
                ImageView img = new ImageView(this);
                img.setImageDrawable(getDrawable(display_mat[i][j].getImg()));

                int finalI = i;
                int finalJ = j;

                if (display_mat[i][j].get_color() != -1) {
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeImages(possibilitiesLayout);

                            sx = finalI;
                            sy = finalJ;
                            setSelected(sx, sy);

                            if (game.check_selection(getSelected()[0], getSelected()[1], turn, 1) == 0) {
                                display_possibilities(getSelected()[0], getSelected()[1]);
                            }
                            else if (game.check_selection(getSelected()[0], getSelected()[1], turn, 1) == 1) {
                                display_possibilities(game.getMustJump()[0], game.getMustJump()[1]);
                            }

                            update();
                        }
                    });
                }
                if (display_mat[i][j].get_direction() == 0)
                    img.setRotation(270);
                else
                    img.setRotation(90);

                RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(100, 100);
                parms.setMargins(x - 50, y, 0, 0);
                parms.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                parms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                img.setLayoutParams(parms);

                x += 100;

                boardLayout.addView(img);

                getSelected();
            }
            y += 100;
            x = 50 * ((i + 1) % 2);
        }
        if((turn %2) == 0){
            nb_jump_w.setText(String.valueOf(game.getJump()));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void display_possibilities(int px, int py) {
        possibilitiesLayout.removeAllViews();
        if (game.getGameboard()[px][py] != null && game.getGameboard()[px][py].get_color() != -1) {
            int[][] pos = null;
            if(game.check_selection(px, py, turn, 1) != -1) {
                pos = game.get_possibilities(game.getGameboard()[px][py], px, py);
            }
            if(pos != null) {
                for (int[] p : pos
                ) {
                    int[] tmp = getrelative_position(p);
                    int y = 0;
                    int x = 0;

                    for (int i = 0; i < 7; i++) {

                        for (int j = 0; j < 9; j++) {
                            if (i == tmp[0] && j == tmp[1]) {
                                ImageView img = new ImageView(this);
                                img.setImageDrawable(getDrawable(R.drawable.hexagone_white));
                                RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(100, 100);
                                int finalI = p[0];
                                int finalJ = p[1];
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        update();
                                        setSelected(finalI, finalJ);
                                        //System.out.println("move depuis:" + getSelected()[0] + getSelected()[1] + "vers :" + finalI + finalJ);
                                        game.move(finalI, finalJ);
                                        removeImages(possibilitiesLayout);
                                        if (game.checkEndTurn() || game.checkEndGame()) {
                                            // System.out.println("fin de tour");
                                            turn ++;
                                            game.end_turn();
                                            layout.setBackground(getDrawable(R.drawable.black_border));

                                            if(game.checkEndGame()){
                                                game = new GameBoard(getApplicationContext());
                                                System.out.println("Fin du game");
                                            }

                                            update();

                                            runIa();


                                            update();

                                            // displayIaMoves();

                                            //game.add_turn();
                                            turn ++;
                                            game.end_turn();

                                            if(game.checkEndGame()){
                                                game = new GameBoard(getApplicationContext());
                                                System.out.println("Fin du game");
                                            }
                                        }
                                        removeImages(possibilitiesLayout);
                                        update();
                                    }
                                });
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

            game.setHas_jumped((byte) iaMove[4]);
            //System.out.println("quil est bete : " + iaMove[5]);

            game.setPossible_jump(setterjump);
            game.setPossible_move(settermove);

            if (iaMove[2] == -1 || iaMove[1] == -1) {
                break;
            }
            // System.out.println("Moving : " + game.move(iaMove[2], iaMove[3]));
            game.set_movedPawn(iaMove[2], iaMove[3]);
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
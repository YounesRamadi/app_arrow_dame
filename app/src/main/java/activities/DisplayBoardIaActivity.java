package activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

import ia.Ia;

import controller.GameBoard;
import controller.Pion;


public class DisplayBoardIaActivity extends AppCompatActivity {

    private GameBoard game;
    private RelativeLayout myLayout;
    private RelativeLayout layout;
    private Pion[][] display_mat = new Pion[7][9];
    private int[] selected = new int[2];
    private int sx;
    private int sy;
    private Button btn;
    private TextView player;
    private TextView nb_jump_w;
    private TextView nb_jump_b;
    private int turn = 0;
    public static final String BUNDLE_STATE_TURN="currentTurn";
    public static final String BUNDLE_STATE_GAMEBOARD="currentGameboard";

    private int[]  iaMove = new int[4];
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

        setContentView(R.layout.activity_display_board_ia);

        this.myLayout = (RelativeLayout) findViewById(R.id.board);
        this.layout = (RelativeLayout) findViewById(R.id.possibilites);

        nb_jump_w = (TextView) findViewById(R.id.nb_jump_w);
        nb_jump_b = (TextView) findViewById(R.id.nb_jump_b);

        Button btn1 = new Button(this);
        btn1 = (Button) findViewById(R.id.endTurn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                game.end_turn();
                turn++;
                //myLayout.removeAllViews();
                turn = 0;
                update();
            }
        });
        Button btn2 = new Button(this);
        btn2 = (Button) findViewById(R.id.endTurn1);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                game.end_turn();
                turn++;
                //myLayout.removeAllViews();
                update();
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
        System.out.println("turn:" + turn);
        // faudrait peut etre trouver autre chose
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);

        //affichage du cadre
/*
        ImageView imgTurn = new ImageView(this);
        imgTurn.setImageDrawable(getDrawable(R.drawable.yellow_haxagone));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1000, 1000);
        params.setMargins(0, -200, 0, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        imgTurn.setLayoutParams(params);
        imgTurn.setRotation(90);
        myLayout.addView(imgTurn);
*/
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
                            sx = finalI;
                            sy = finalJ;
                            setSelected(sx, sy);
                            removeImages(layout);
                            if (game.check_selection(getSelected()[0], getSelected()[1], turn, 1) == 0) {
                                display_possibilities(getSelected()[0], getSelected()[1]);
                            }
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

                myLayout.addView(img);

                getSelected();
                String s = sx + ":" + selected[1];
                TextView t = (TextView) findViewById(R.id.selected);
                t.setText(s);
            }
            y += 100;
            x = 50 * ((i + 1) % 2);
        }
        if((turn %2) == 0){
            nb_jump_b.setText("0");
            nb_jump_w.setText(String.valueOf(game.getJump()));
        }
        else if((turn %2) == 1){
            nb_jump_w.setText("0");
            nb_jump_b.setText(String.valueOf(game.getJump()));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void display_possibilities(int px, int py) {
        layout.removeAllViews();
        if (game.getGameboard()[px][py] != null && game.getGameboard()[px][py].get_color() != -1) {
            int[][] pos = null;
            if(game.check_selection(px, py, turn, 1) != -1) {
                pos = game.get_possibilities(game.getCell(px, py), px, py);
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
                                img.setImageDrawable(getDrawable(R.drawable.yellow_haxagone));
                                RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(100, 100);
                                int finalI = p[0];
                                int finalJ = p[1];
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setSelected(finalI, finalJ);
                                        System.out.println("move depuis:" + getSelected()[0] + getSelected()[1] + "vers :" + finalI + finalJ);
                                        game.move(finalI, finalJ);
                                        update();
                                        layout.removeAllViews();
                                        removeImages(myLayout);
                                        removeImages(myLayout);
                                        if(game.checkEndTurn()){
                                            System.out.println("fin de tour");
                                            game.end_turn();
                                            turn ++;
                                            update();
                                            //jump = 0
                                            do {
                                                iaMove = ia.minMax((byte) (1), game.copy(), 1);
                                                //jump = 0
                                                System.out.println("Taking " + iaMove[0] + " : " + iaMove[1]);
                                                System.out.println("Going in " + iaMove[2] + " : " + iaMove[3]);
                                                game.setSelection(iaMove[0], iaMove[1]);
                                                //jump = 0
                                                int[][] setterjump = new int[1][2];
                                                setterjump[0][0] = iaMove[2];
                                                setterjump[0][1] = iaMove[3];

                                                int[][] settermove = new int[1][2];
                                                settermove[0][0] = iaMove[2];
                                                settermove[0][1] = iaMove[3];

                                                game.setHas_jumped((byte) iaMove[4]);
                                                System.out.println("quil est bete : " + iaMove[5]);

                                                game.setPossible_jump(setterjump);
                                                game.setPossible_move(settermove);
                                                if(iaMove[2] == -1 || iaMove[1] == -1){
                                                    break;
                                                }
                                                System.out.println("Moving : " + game.move(iaMove[2], iaMove[3]));
                                                System.out.println("Flags h_j :" + iaMove[4] + "/ j :" + iaMove[5]);
                                                update();

                                            }while(iaMove[5] >= 1 || iaMove[4] == (byte)1);

                                            turn ++;
                                            game.end_turn();
                                            //game.add_turn();


                                            if(game.end_turn()==(byte)1) {
                                                System.out.println("Test");
                                                game = new GameBoard(getApplicationContext());
                                                //game.initGameBoard();
                                            }

                                            update();
                                        }
                                        update();
                                    }
                                });
                                parms.setMargins(x - 50, y, 0, 0);
                                parms.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                parms.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                img.setLayoutParams(parms);

                                layout.addView(img);
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
        for (int pos = 0; pos < layout.getChildCount(); pos++) {
            if (layout.getChildAt(pos) instanceof ImageView) {
                layout.removeView(layout.getChildAt(pos));
            }
        }
    }


}
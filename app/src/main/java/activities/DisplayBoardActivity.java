package activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apadnom.R;

<<<<<<<< HEAD:app/src/main/java/activities/DisplayBoardActivity.java
import controller.GameBoard;
import controller.Pion;
import ia.Ia;

========
>>>>>>>> ae12517 (merge branches ia et antonin):app/src/main/java/controller/DisplayBoardActivity.java

public class DisplayBoardActivity extends AppCompatActivity {

    private GameBoard game;
    private AbsoluteLayout myLayout;
    private AbsoluteLayout layout;
    private Pion[][] display_mat = new Pion[7][9];
    private int[] selected = new int[2];
    private int sx;
    private int sy;
    private Button btn;
    private TextView player;
    private TextView nb_jump_w;
    private TextView nb_jump_b;
    private int turn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_board);

        this.myLayout = (AbsoluteLayout) findViewById(R.id.layout);
        this.layout = (AbsoluteLayout) findViewById(R.id.head);
        game = new GameBoard(getApplicationContext());

        nb_jump_w = (TextView) findViewById(R.id.nb_jump_w);
        nb_jump_b = (TextView) findViewById(R.id.nb_jump_b);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.initGameBoard();
                //myLayout.removeAllViews();
<<<<<<<< HEAD:app/src/main/java/activities/DisplayBoardActivity.java
                turn = 0;
                update();
            }
        });
        Button btn1 = new Button(this);
        btn1 = (Button) findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                game.end_turn();
                turn++;
                //myLayout.removeAllViews();
========
>>>>>>>> ae12517 (merge branches ia et antonin):app/src/main/java/controller/DisplayBoardActivity.java
                update();
            }
        });

        player = (TextView) findViewById(R.id.playerTurn);
        update();

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
        // faudrait peut etre trouver autre chose
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);
        removeImages(myLayout);

        this.display_mat = game.display();
        int y = 0;
        int x = 0;
        for (int i = 0; i < 7; i++) {
            y += 100;
            x = 50 * (i % 2);
            for (int j = 0; j < 9; j++) {
                if (display_mat[i][j] == null) {
                    x += 100;
                    continue;
                }
                ImageView img = new ImageView(this);
                img.setImageDrawable(getDrawable(display_mat[i][j].getImg()));
                AbsoluteLayout.LayoutParams parms = new AbsoluteLayout.LayoutParams(100, 100, x, y);
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
<<<<<<<< HEAD:app/src/main/java/activities/DisplayBoardActivity.java
                            if (game.check_selection(getSelected()[0], getSelected()[1], turn, 1) == 0) {
                                display_possibilities(getSelected()[0], getSelected()[1]);
                            }
========
                            display_possibilities(getSelected()[0], getSelected()[1]);
>>>>>>>> ae12517 (merge branches ia et antonin):app/src/main/java/controller/DisplayBoardActivity.java
                        }
                    });
                }
                if (display_mat[i][j].get_direction() == 0)
                    img.setRotation(270);
                else
                    img.setRotation(90);
                img.setLayoutParams(parms);

                x += 100;

                myLayout.addView(img);

                getSelected();
                String s = sx + ":" + selected[1];
                TextView t = (TextView) findViewById(R.id.selected);
                t.setText(s);
            }
        }
        if ((turn % 2) == 0) {
            nb_jump_b.setText("0");
            nb_jump_w.setText(String.valueOf(game.getJump()));
        } else if ((turn % 2) == 1) {
            nb_jump_w.setText("0");
            nb_jump_b.setText(String.valueOf(game.getJump()));
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void display_possibilities(int px, int py) {
        layout.removeAllViews();
        if (game.getGameboard()[px][py] != null && game.getGameboard()[px][py].get_color() != -1) {
            int[][] pos = null;
            if (game.check_selection(px, py, turn, 1) != -1) {
                pos = game.get_possibilities(game.getCell(px, py), px, py);
            }
            if (pos != null) {
                for (int[] p : pos
                ) {
                    int[] tmp = getrelative_position(p);
                    int y = 0;
                    int x = 0;

                    for (int i = 0; i < 7; i++) {
                        y += 100;
                        x = 50 * (i % 2);
                        for (int j = 0; j < 9; j++) {
                            if (i == tmp[0] && j == tmp[1]) {
                                ImageView img = new ImageView(this);
                                img.setImageDrawable(getDrawable(R.drawable.yellow_haxagone));
                                AbsoluteLayout.LayoutParams parms = new AbsoluteLayout.LayoutParams(100, 100, x, y);
                                img.setLayoutParams(parms);
                                int finalI = p[0];
                                int finalJ = p[1];
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setSelected(finalI, finalJ);
                                        game.move(finalI, finalJ);
                                        layout.removeAllViews();
                                        removeImages(myLayout);
                                        removeImages(myLayout);
<<<<<<<< HEAD:app/src/main/java/activities/DisplayBoardActivity.java
                                        if (game.checkEndTurn()) {
                                            System.out.println("fin de tour");
                                            turn++;
                                            update();
                                            iaMove = ia.minMax((byte) (turn % 2), new GameBoard(game, getApplicationContext()), 2);
                                            game.setSelection(iaMove[0], iaMove[1]);
                                            game.move(iaMove[2], iaMove[3]);
                                            turn++;
                                            System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());
                                            game.end_turn();
                                            game.add_turn();
                                            System.out.println("zobturn" + turn);
                                            /*
                                            if(game.end_turn()==(byte)1) {
                                                System.out.println("Test");
                                                game.initGameBoard();
                                            }
                                            */

                                            update();
                                        }
========
>>>>>>>> ae12517 (merge branches ia et antonin):app/src/main/java/controller/DisplayBoardActivity.java
                                        update();
                                    }
                                });
                                layout.addView(img);
                            }
                            x += 100;
                        }
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

    public void removeImages(AbsoluteLayout layout) {
        for (int pos = 0; pos < layout.getChildCount(); pos++) {
            if (layout.getChildAt(pos) instanceof ImageView) {
                layout.removeView(layout.getChildAt(pos));
            }
        }
    }


}
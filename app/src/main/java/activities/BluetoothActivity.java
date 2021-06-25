package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apadnom.R;
import com.muddzdev.styleabletoast.StyleableToast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.UUID;

import controller.BluetoothConnectionService;
import controller.DeviceListAdapter;
import controller.GameBoard;
import controller.Pion;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private GameBoard game;
    private String gameString = "";
    private byte[] msg;
    private Button mhost;
    private Button mconnect;
    private int playerT;
    private int turn;
    private Pion[][] display_mat = new Pion[7][9];

    private LinearLayout layout;
    private RelativeLayout boardLayout;
    private RelativeLayout possibilitiesLayout;
    private int[] selected = new int[2];
    private int sx;
    private int sy;
    private Button btn;
    private TextView player;
    private TextView nb_jump;
    private ImageView whiteScore;
    private ImageView blackScore;
    private Toast toast;

    public void runGame(){

        this.layout = findViewById(R.id.layout);
        this.boardLayout = (RelativeLayout) findViewById(R.id.board);
        this.possibilitiesLayout = (RelativeLayout) findViewById(R.id.possibilites);
        whiteScore = findViewById(R.id.scoreW);
        blackScore = findViewById(R.id.scoreB);
        nb_jump = (TextView) findViewById(R.id.nb_jump_w);

        Button btn1 = new Button(this);
        btn1 = (Button) findViewById(R.id.endTurn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());
                if((game.getHas_jumped() == (byte)1) && (game.getJump() <1)){
                    System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());
                    game.end_turn();
                    turn++;
                    turn = 0;
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

    private static final String TAG = "BluetoothActitivty";

    BluetoothAdapter mBluetoothAdapter;
    BluetoothConnectionService mBluetoothConnection;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    BluetoothDevice mBTDevice;

    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    private ListView lvNewDevices;

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // When discovery finds a device
        if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);
            switch(state){
                case BluetoothAdapter.STATE_OFF:
                    Log.d(TAG, "onReceive: STATE OFF");
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                    break;
            }
        }
        }
    };

    /**
     * Broadcast Receiver for changes made to bluetooth states such as:
     * 1) Discoverability mode on/off or expire.
     */
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };




    /**
     * Broadcast Receiver for listing devices that are not yet paired
     * -Executed by btnDiscover() method.
     */
    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");
            int tmp =1;
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                for(int i=0;i < mBTDevices.size();i++){
                    if(mBTDevices.get(i).getAddress().compareTo(device.getAddress())==0){
                        tmp = 0;
                    }
                }
                if(tmp==1 && device.getName()!=null){
                    mBTDevices.add(device);
                }

                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };

    /**
     * Broadcast Receiver that detects bond state changes (Pairing status changes)
     */
    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    //inside BroadcastReceiver4
                    mBTDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
        //mBluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GameBoard(getApplicationContext());

        setContentView(R.layout.activity_bluetooth);

        mhost = (Button) findViewById(R.id.host);
        mconnect = (Button) findViewById(R.id.connect);
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);

        mBTDevices = new ArrayList<>();

        //Broadcasts when bond state changes (ie:pairing)
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(BluetoothActivity.this);

        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);

        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!mBluetoothAdapter.isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,new IntentFilter("send"));
        mhost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_display_board_ia);
                runGame();
                playerT = 1;

                turn = 0;
                update();
                StyleableToast.makeText(BluetoothActivity.this,"Waiting for connection...", Toast.LENGTH_SHORT,R.style.exampleToast).show();
            }
        });

        mconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startConnection();
                setContentView(R.layout.activity_display_board_ia);
                playerT = 0;
                turn = 0;
                runGame();
                // 0 c'est le tour de ce joueur

            }
        });
    }
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra("send");
        Log.d(TAG,"DANS LE MAIN : " + text);
        game.setGameboard(text);
        if(text.charAt(text.length() - 1) == '1'){
            turn ++;
        }
        update();
    }
    };
    //create method for starting connection
//***remember the conncction will fail and app will crash if you haven't paired first
    public void startConnection(){
        startBTConnection(mBTDevice,MY_UUID_INSECURE);
    }

    /**
     * starting chat service method
     */
    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device,uuid);
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     *
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    private void checkBTPermissions() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive.
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You Clicked on a device.");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);
        StyleableToast.makeText(this,"You selected " + deviceName, Toast.LENGTH_SHORT,R.style.exampleToast).show();
        //create the bond.
        //NOTE: Requires API 17+? I think this is JellyBean
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(TAG, "Trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();

            mBTDevice = mBTDevices.get(i);
            mBluetoothConnection = new BluetoothConnectionService(BluetoothActivity.this);
        }
    }

    /**
     * Update the board display
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void update() {
        updateTurn();
        updateScores();
        // Cleaning the layout
        removeImages(boardLayout);

        // Debug
        System.out.println("turn:" + turn);

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
                if (display_mat[i][j].get_color() == playerT) {
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
        if ((turn % 2) == playerT) {
            nb_jump.setText(String.valueOf(game.getJump()));
        } else if ((turn % 2) == 1) {
            nb_jump.setText("0");
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

                    int[] tmp = getrelative_position(p);

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
                                            gameString = game.toString() + "1";
                                            turn ++;
                                            msg = gameString.getBytes(Charset.defaultCharset());
                                            System.out.println(msg);
                                            mBluetoothConnection.write(msg);
                                            //Debug
                                            System.out.println("white : " + game.getNb_B_stars() + " black " + game.getNb_W_stars());

                                            game.end_turn();

                                            if (game.checkEndGame()) {
                                                System.out.println("Test");
                                                game = new GameBoard(getApplicationContext());
                                            }
                                            update();
                                        }

                                        else{
                                            gameString = game.toString() + "0";
                                            msg = gameString.getBytes(Charset.defaultCharset());
                                            System.out.println(msg);

                                            mBluetoothConnection.write(msg);

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

    public void updateTurn(){
        if(turn%2 == 0){
            layout.setBackground(getDrawable(R.drawable.white_border));
        }
        else{
            layout.setBackground(getDrawable(R.drawable.black_border));
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

}
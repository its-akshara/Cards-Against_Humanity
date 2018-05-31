package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.pm.PackageManager;
import android.widget.TextView;
import java.util.Random;
import java.util.HashMap;
import android.content.Intent;
import playerInformation.Player;
import cahCardParser.cardParser;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate.Status;
import com.google.android.gms.nearby.connection.Strategy;
import android.content.Context;
import android.Manifest;
import static java.nio.charset.StandardCharsets.UTF_8;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class PlayerActivity extends AppCompatActivity {

    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;
    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;

    // Our handle to Nearby Connections
    private ConnectionsClient connectionsClient;
    //private String opponentEndpointId;

    TextView playerIDDisplay;
    TextView blackCardQuestion;
    TextView noCardChosen;
    TextView currentRound;
    //static int count = 1;
    final int CARD_NUMBER = 4;
    int selectedCard = -1;
    CharSequence Question = "What college do you go to?";
    TextView[] cards = new TextView[CARD_NUMBER];
    CharSequence[] cardText = new CharSequence[CARD_NUMBER];
    cardParser cp = new cardParser();
    int numBlackCards = cp.getNumberOfBlackCards();
    int numWhiteCards = cp.getNumberOfWhiteCards();
    Random rand = new Random();
    Button update;
    Player player;
    Bundle previousActivityInfo;
    //Stores cardNo->Index of Card in Parser
    HashMap<Integer, Integer> cardToParseIndex = new HashMap<Integer, Integer>();

    private static final String TAG = "CardsAgainstM117";


    // Callbacks for receiving payloads
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {
                    //Nothing
                }


                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    //nothing
                    }

            };

// Callbacks for connections to other devices
private final ConnectionLifecycleCallback connectionLifecycleCallback =
        new ConnectionLifecycleCallback() {
@Override
public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
        Log.i(TAG, "onConnectionInitiated: accepting connection");
        connectionsClient.acceptConnection(endpointId, payloadCallback);
       // opponentName = connectionInfo.getEndpointName();
        }

@Override
public void onConnectionResult(String endpointId, ConnectionResolution result) {
        if (result.getStatus().isSuccess()) {
        Log.i(TAG, "onConnectionResult: connection successful");

        connectionsClient.stopDiscovery();
        connectionsClient.stopAdvertising();

       // opponentEndpointId = endpointId;
        } else {
        Log.i(TAG, "onConnectionResult: connection failed");
        }
        }

@Override
public void onDisconnected(String endpointId) {
        Log.i(TAG, "onDisconnected: disconnected from the opponent");
        }
        };


    /*
    protected void hardCode()
    {
        cardText[0] = "USC";
        cardText[1] = "UCLA";
    }
    */

    // Callbacks for finding other devices
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i("CardsAgainstM117", "onEndpointFound: endpoint found, connecting");
                    connectionsClient.requestConnection(player.getPlayerAsString(), endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {}
            };

    /** Returns true if the app was granted all the permissions. Otherwise, returns false. */
    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();

        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
    }

    /** Handles user acceptance (or denial) of our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this,"Do not have sufficient permissions", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        recreate();
    }

    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
                getPackageName(), endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY));
    }


    /** Broadcasts our presence using Nearby Connections so other players can find us. */
    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                player.getPlayerAsString(), getPackageName(), connectionLifecycleCallback, new AdvertisingOptions(STRATEGY));
    }


    protected void update(){
        //randomly select one black card
        int bCardID = rand.nextInt(numBlackCards);
        Question = cp.getBlackCardByIndex(bCardID);

        //get round
        currentRound = (TextView)findViewById(R.id.round);
        currentRound.setText("Round "+player.getRound());

        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackQuestion);
        blackCardQuestion.setText(Question);

        cards[0] = (TextView) findViewById(R.id.card0);
        cards[1] = (TextView) findViewById(R.id.card1);
        cards[2] = (TextView) findViewById(R.id.card2);
        cards[3] = (TextView) findViewById(R.id.card3);

        //need to display cards you can choose
        for(int i = 0; i<CARD_NUMBER; i++)
        {
            int wCardID = rand.nextInt(numWhiteCards);
            cardText[i] = cp.getWhiteCardByIndex(wCardID);

        }


        //JSON parser stuff


        cards[0].setText(cardText[0]);
        cards[1].setText(cardText[1]);
        cards[2].setText(cardText[2]);
        cards[3].setText(cardText[3]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Select a White Card");

        connectionsClient = Nearby.getConnectionsClient(this);

        //hardCode();
        player = new Player();
        previousActivityInfo = getIntent().getExtras();
        player.setPlayerID(previousActivityInfo.getInt("PLAYER_ID"));
        player.setRound(previousActivityInfo.getInt("ROUND"));

        playerIDDisplay= (TextView) findViewById(R.id.playerID);
        playerIDDisplay.setText("Player " + (1+player.getPlayer()));

        currentRound = (TextView)findViewById(R.id.round);
        currentRound.setText("Round "+(player.getRound()+1));

        //randomly select one black card
        int bCardID = rand.nextInt(numBlackCards);
        Question = cp.getBlackCardByIndex(bCardID);

        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackQuestion);
        blackCardQuestion.setText(Question);

        cards[0] = (TextView) findViewById(R.id.card0);
        cards[1] = (TextView) findViewById(R.id.card1);
        cards[2] = (TextView) findViewById(R.id.card2);
        cards[3] = (TextView) findViewById(R.id.card3);

        int[] cardIDs = new int [CARD_NUMBER];

        //need to display cards you can choose
        for(int i = 0; i<CARD_NUMBER; i++)
        {
            int wCardID = rand.nextInt(numWhiteCards);

            boolean isRepeat;

            do {
                isRepeat = false;

                for (int ii = 0; ii < i; ii++) {
                    if (cardIDs[ii] == wCardID)
                        isRepeat = true;
                }

                if(isRepeat)
                    wCardID = rand.nextInt(numWhiteCards);
            } while(isRepeat);

            cardText[i] = cp.getWhiteCardByIndex(wCardID);
            cardToParseIndex.put(i,wCardID);

        }


        //JSON parser stuff


        cards[0].setText(cardText[0]);
        cards[1].setText(cardText[1]);
        cards[2].setText(cardText[2]);
        cards[3].setText(cardText[3]);

        //set up on click listeners
        cards[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards[0].setBackgroundColor(Color.parseColor("#34546b"));
                if(selectedCard != -1 && selectedCard != 0){
                    cards[selectedCard].setBackgroundColor(Color.parseColor("#fafafa"));
                }
                selectedCard = 0;
            }
        });

        cards[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards[1].setBackgroundColor(Color.parseColor("#34546b"));
                if(selectedCard != -1 && selectedCard != 1){
                    cards[selectedCard].setBackgroundColor(Color.parseColor("#fafafa"));
                }
                selectedCard = 1;
            }
        });

        cards[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards[2].setBackgroundColor(Color.parseColor("#34546b"));
                if(selectedCard != -1 && selectedCard != 2){
                    cards[selectedCard].setBackgroundColor(Color.parseColor("#fafafa"));
                }
                selectedCard = 2;
            }
        });

        cards[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards[3].setBackgroundColor(Color.parseColor("#34546b"));
                if(selectedCard != -1 && selectedCard != 3){
                    cards[selectedCard].setBackgroundColor(Color.parseColor("#fafafa"));
                }
                selectedCard = 3;
            }
        });

        update = (Button) findViewById(R.id.reset);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCard == -1) {
                    noCardChosen = findViewById(R.id.selectACard);
                    noCardChosen.setText("Please choose a card!");
                } else{
                    //byte array for information transfer
                    byte[] selectedCardInfo = {((byte) player.getPlayer()), cardToParseIndex.get(selectedCard).byteValue()};

                    Intent i = new Intent(getApplicationContext(), LoadingActivity.class);
                    i.putExtra("PLAYER_ID",player.getPlayer());
                    i.putExtra("ROUND",player.getRound());
                    startActivity(i);
                }
            }
        });
    }
}

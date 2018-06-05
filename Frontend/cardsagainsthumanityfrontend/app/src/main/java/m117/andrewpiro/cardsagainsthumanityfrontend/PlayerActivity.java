package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import com.google.android.gms.tasks.*;
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
import com.google.android.gms.common.api.GoogleApiClient;
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

//tags are needed in order to identify the application
    private static final String TAG = "CardsAgainstM117";

    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };
//set up
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

//setting up n->m connection bc we need cluster
    private static final Strategy STRATEGY = Strategy.P2P_STAR;

    // Our handle to Nearby Connections
    private ConnectionsClient connectionsClient;


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

    boolean receivedResult = false;
    String opponentEndpointId ;
    String currOpponentEndpointId;
    int count = 0;
    int winner = -1;
   // public GoogleApiClient mGoogleApiClient;
    //Stores cardNo->Index of Card in Parser
    HashMap<Integer, Integer> cardToParseIndex = new HashMap<Integer, Integer>();


    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                //this is from nearby connections
                //function that we are overriding in order for what to do when we receive payload
                //we only need this one and not transfer bc transfer is for file/stream
                public void onPayloadReceived(String endpointId, Payload payload) {
                    byte[] res = payload.asBytes();
                    winner = res[2];
                    if(winner!=10)
                    {
                        CharSequence text = "Winner is "+Integer.toString(winner);
                        //create toast (pop up window) and last for a while to show winner
                        Toast.makeText(getApplicationContext(), text,Toast.LENGTH_LONG).show();
                        receivedResult = true;
                        player.updatePlayer();
                        if (player.isJudge()) {
                            Intent i = new Intent(getApplicationContext(), JudgeActivity2.class);
                            i.putExtra("PLAYER_ID", player.getPlayer());
                            i.putExtra("ROUND", player.getRound());
                            startActivity(i);
                        } else {
                            finish();
                            Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
                            i.putExtra("PLAYER_ID", player.getPlayer());
                            i.putExtra("ROUND", player.getRound());
                            startActivity(i);
                        }
                    }

                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {

                    }

            };

    // Callbacks for finding other devices
    //on end point found we found the thing we need to connect to
    //request a connection with what we found (this is my ID, ID of the connection of where you are, etc.)
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
//                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
//                    Log.i(TAG, "onEndpointFound: endpoint found, connecting");
//                    Nearby.getConnectionsClient(getApplicationContext()).requestConnection(player.getPlayerAsString(),endpointId,connectionLifecycleCallback);
//                }
                public void onEndpointFound(
                        String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo) {
                    Nearby.getConnectionsClient(getApplicationContext()).requestConnection(
                            player.getPlayerAsString(),
                            endpointId,
                            connectionLifecycleCallback)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unusedResult) {
                                            // We successfully requested a connection. Now both sides
                                            // must accept before the connection is established.
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Nearby Connections failed to request the connection.
                                            Log.i(TAG,e.getMessage());
                                        }
                                    });
                }



                @Override
                public void onEndpointLost(String endpointId) {

                    Log.i(TAG,"Endpoint lost.");
                } //don't care for right now
            };

    // Callbacks for connections to other devices
    //this is what we're going to do when we have a connection
    //during the life cycle of the connection this is what we do (callback)
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i(TAG, "onConnectionInitiated: accepting connection");
                    //connectionsClient.acceptConnection(endpointId, payloadCallback);
                    Nearby.getConnectionsClient(getApplicationContext()).acceptConnection(endpointId,payloadCallback);
                    //someone else has started connection so we need to accept
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "onConnectionResult: connection successful");
                        Log.i(TAG, endpointId+" is the player we're getting data from");
                        currOpponentEndpointId = endpointId;
                        opponentEndpointId = endpointId;
                        connectionsClient.stopDiscovery();
                        //connectionsClient.stopAdvertising();
                        //bc we have multiple and we want to keep them going
                        //if this is successful note the success

                    } else {
                        Log.i(TAG, "onConnectionResult: connection failed");
                        //otherwise FAILURE AND TELL EVERYONE
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.i(TAG, "onDisconnected: disconnected from the opponent");
                    //boo, note.
                }
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
                //make a toast if stuff doesn't work bc bread makes everything better
                Toast.makeText(this, "Missing permissions!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        recreate();
    }

    /** Starts looking for other players using Nearby Connections. */
//    private void startDiscovery() {
//        Log.i(TAG, "Player: Started discovering");
//        Nearby.getConnectionsClient(getApplicationContext()).startDiscovery( getPackageName(),endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY));
//
//    }
    private void startDiscovery() {
        Nearby.getConnectionsClient(getApplicationContext()).startDiscovery(
                getPackageName(),
                endpointDiscoveryCallback,
                new DiscoveryOptions(STRATEGY))
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unusedResult) {
                                // We're discovering!
                                Log.i(TAG,"PLAYER: Started Discovering");
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // We were unable to start discovering.
                                Log.i(TAG,e.getMessage());
                            }
                        });
    }

    /** Broadcasts our presence using Nearby Connections so other players can find us. */
    private void startAdvertising() {
        Log.i(TAG, "Player: Started advertising");
        Nearby.getConnectionsClient(getApplicationContext()).startAdvertising(player.getPlayerAsString(), getPackageName(), connectionLifecycleCallback, new AdvertisingOptions(STRATEGY));

    }

    @TargetApi(Build.VERSION_CODES.M)

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();

        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
    }

    @Override
    protected void onStop() {
        connectionsClient.stopAllEndpoints();

        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Select a White Card");

        Log.i(TAG,"OnCreate() has been called.");

        //hardCode();
        player = new Player();
        previousActivityInfo = getIntent().getExtras();
        player.setPlayerID(previousActivityInfo.getInt("PLAYER_ID"));
        player.setRound(previousActivityInfo.getInt("ROUND"));
        connectionsClient = Nearby.getConnectionsClient(this);

        playerIDDisplay= (TextView) findViewById(R.id.playerID);
        playerIDDisplay.setText("Player " + (1+player.getPlayer()));

        currentRound = (TextView)findViewById(R.id.round);
        currentRound.setText("Round "+(player.getRound()+1));

        //startAdvertising();
        startDiscovery();


        //randomly select one black card
        int bCardID = player.getRound();
        Question = cp.getBlackCardByIndex(bCardID);

        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackQuestion);
        blackCardQuestion.setText(Question);

        cards[0] = (TextView) findViewById(R.id.card0);
        cards[1] = (TextView) findViewById(R.id.card1);
        cards[2] = (TextView) findViewById(R.id.card2);
        cards[3] = (TextView) findViewById(R.id.card3);

        final int[] cardIDs = new int [CARD_NUMBER];

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

            cardIDs[i] = wCardID;
            cardText[i] = cp.getWhiteCardByIndex(wCardID);

        }


        connectionsClient = Nearby.getConnectionsClient(this);

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
                cardToParseIndex.put(selectedCard,cardIDs[selectedCard]);
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
                cardToParseIndex.put(selectedCard,cardIDs[selectedCard]);
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
                cardToParseIndex.put(selectedCard,cardIDs[selectedCard]);
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
                cardToParseIndex.put(selectedCard,cardIDs[selectedCard]);
            }
        });

        update = (Button) findViewById(R.id.reset);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCard == -1) {
                    noCardChosen = findViewById(R.id.selectACard);
                    noCardChosen.setText("Please choose a card!");
                }  else {
                    //byte array for information transfer

                    byte[] selectedCardInfo = {((byte) player.getPlayer()), cardToParseIndex.get(selectedCard).byteValue(),10};
                        Nearby.getConnectionsClient().sendPayload(opponentEndpointId,Payload.fromBytes(selectedCardInfo));
                    Log.i(TAG, "Sent Payload");
                    update.setText("Sent Card");

                    if(receivedResult)
                    {
                        player.updatePlayer();
//                        connectionsClient.stopDiscovery();
                        //connectionsClient.stopAdvertising();
                        if (player.isJudge()) {
                            Intent i = new Intent(getApplicationContext(), JudgeActivity2.class);
                            i.putExtra("PLAYER_ID", player.getPlayer());
                            i.putExtra("ROUND", player.getRound());
                            startActivity(i);
                        } else {
                        Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
                        i.putExtra("PLAYER_ID", player.getPlayer());
                        i.putExtra("ROUND", player.getRound());
                        startActivity(i);
                        }
                    }

                }
            }
        });
    }
}

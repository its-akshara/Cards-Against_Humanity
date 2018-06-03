package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.nearby.connection.Strategy;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import playerInformation.Player;
import java.util.Random;

import cahCardParser.cardParser;

public class JudgeActivity2 extends AppCompatActivity {
    TextView playerIDDisplay;
    TextView blackCardQuestion;
    TextView errorMessage;
    static int count = 1;
    final int CARD_NUMBER = 2;
    int selectedCard = -1;
    CharSequence Question;
    TextView[] cards = new TextView[CARD_NUMBER];
    CharSequence[] cardText = new CharSequence[CARD_NUMBER];
    cardParser cp = new cardParser();
    int numBlackCards;
    int numWhiteCards;
    Random rand = new Random();
    Button confirm;
    Bundle previousActivityInfo;
    Player player;
    byte[] receivedPlayerCards;
//    public GoogleApiClient mGoogleApiClient;

    //Copied from Player Activity
    private static final String TAG = "CardsAgainstM117";

    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADMIN,
                    android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };

    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    private static final Strategy STRATEGY = Strategy.P2P_CLUSTER;

    // Our handle to Nearby Connections
    private ConnectionsClient connectionsClient;

    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                //this is from nearby connections
                //function that we are overriding in order for what to do when we receive payload
                //we only need this one and not transfer bc transfer is for file/stream
                public void onPayloadReceived(String endpointId, Payload payload) {
                    byte[] receivedCard = payload.asBytes();
                    cards[0] = (TextView) findViewById(R.id.player1Card);
                    cards[1] = (TextView) findViewById(R.id.player2Card);
                    cardText[receivedCard[0]] = cp.getWhiteCardByIndex(receivedCard[1]);
                    cards[receivedCard[0]].setText(cardText[receivedCard[0]]);

                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {

                }

            };
    private final EndpointDiscoveryCallback endpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override
                public void onEndpointFound(String endpointId, DiscoveredEndpointInfo info) {
                    Log.i(TAG, "onEndpointFound: endpoint found, connecting");
                    connectionsClient.requestConnection(player.getPlayerAsString(), endpointId, connectionLifecycleCallback);
                }

                @Override
                public void onEndpointLost(String endpointId) {} //don't care for right now
            };

    // Callbacks for connections to other devices
    //this is what we're going to do when we have a connection
    //during the life cycle of the connection this is what we do (callback)
    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    Log.i(TAG, "onConnectionInitiated: accepting connection");
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    //someone else has started connection so we need to accept
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    if (result.getStatus().isSuccess()) {
                        Log.i(TAG, "onConnectionResult: connection successful");

                        // connectionsClient.stopDiscovery();
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
    private void startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startDiscovery(
                getPackageName(), endpointDiscoveryCallback, new DiscoveryOptions(STRATEGY));
        //we send package to ensure we're both of the same thing
    }

    /** Broadcasts our presence using Nearby Connections so other players can find us. */
    private void startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient.startAdvertising(
                player.getPlayerAsString(), getPackageName(), connectionLifecycleCallback, new AdvertisingOptions(STRATEGY));
        //send our id, our package name, (above), both endpoints can be hubs and spokes simult.
        //hubs: send out information
        //spokes: receive information
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendPayload(String endpointId, Payload payload) {
        if (payload.getType() == Payload.Type.BYTES) {
            // No need to track progress for bytes.
            return;
        }
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
        setContentView(R.layout.activity_judge2);

        //API stuff
        //API stuff
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(Nearby.CONNECTIONS_API)
//                .build();



        getSupportActionBar().setTitle("Choose a Winner");
        numBlackCards = cp.getNumberOfBlackCards();
        numWhiteCards = cp.getNumberOfWhiteCards();

        player = new Player();
        previousActivityInfo = getIntent().getExtras();
        player.setRound(previousActivityInfo.getInt("ROUND"));
        player.setPlayerID(previousActivityInfo.getInt("PLAYER_ID"));

        //randomly select one black card
        int bCardID = rand.nextInt(numBlackCards);
        Question = cp.getBlackCardByIndex(bCardID);


        cards[0] = (TextView) findViewById(R.id.player1Card);
        cards[1] = (TextView) findViewById(R.id.player2Card);

        //need to display cards you can choose
//        for(int i = 0; i<CARD_NUMBER; i++)
//        {
//            int wCardID = rand.nextInt(numWhiteCards);
//            cardText[i] = cp.getWhiteCardByIndex(wCardID);
//
//        }


        //JSON parser stuff


        cards[0].setText("");
        cards[1].setText("");


        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackQuestion);
        blackCardQuestion.setText(Question);


        startAdvertising();
        startDiscovery();



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

        confirm = (Button) findViewById(R.id.reset);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCard == -1) {
                    errorMessage = (TextView) findViewById(R.id.noCardChoosen);
                    errorMessage.setText("Please choose a winner!");
                } else {

                    byte[] winningPlayer = {(byte)selectedCard};


                    Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
                    i.putExtra("PLAYER_ID", player.getPlayer());
                    i.putExtra("ROUND", player.getRound());
                    startActivity(i);
                }
            }
        });



    }
}

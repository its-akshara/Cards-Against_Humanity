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
   // public GoogleApiClient mGoogleApiClient;
    //Stores cardNo->Index of Card in Parser
    HashMap<Integer, Integer> cardToParseIndex = new HashMap<Integer, Integer>();



    /*
    protected void hardCode()
    {
        cardText[0] = "USC";
        cardText[1] = "UCLA";
    }
    */

    // Callbacks for finding other devices

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

        //API stuff
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(Nearby.CONNECTIONS_API)
//                .build();


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
            
            cardIDs[i] = wCardID;
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
                } else {
                    //byte array for information transfer

                    byte[] selectedCardInfo = {((byte) player.getPlayer()), cardToParseIndex.get(selectedCard).byteValue()};


                    player.updatePlayer();
                    if (player.isJudge()) {
                        Intent i = new Intent(getApplicationContext(), JudgeActivity2.class);
                        i.putExtra("PLAYER_ID", player.getPlayer());
                        i.putExtra("ROUND", player.getRound());
                        startActivity(i);
                    } else {
//                        Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
//                        i.putExtra("PLAYER_ID", player.getPlayer());
//                        i.putExtra("ROUND", player.getRound());
//                        startActivity(i);
                    }

//                    Intent i = new Intent(getApplicationContext(), LoadingActivity.class);
//                    i.putExtra("PLAYER_ID",player.getPlayer());
//                    i.putExtra("ROUND",player.getRound());
//                    startActivity(i);
                }
            }
        });
    }
}

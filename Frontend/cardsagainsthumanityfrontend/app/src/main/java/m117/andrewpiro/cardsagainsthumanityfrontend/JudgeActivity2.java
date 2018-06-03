package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.Strategy;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
//    public GoogleApiClient mGoogleApiClient;

    //Copied from Player Activity
    private static final String TAG = "CardsAgainstM117";

    private static final String[] REQUIRED_PERMISSIONS =
            new String[] {
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
        for(int i = 0; i<CARD_NUMBER; i++)
        {
            int wCardID = rand.nextInt(numWhiteCards);
            cardText[i] = cp.getWhiteCardByIndex(wCardID);

        }


        //JSON parser stuff


        cards[0].setText(cardText[0]);
        cards[1].setText(cardText[1]);


        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackQuestion);
        blackCardQuestion.setText(Question);



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

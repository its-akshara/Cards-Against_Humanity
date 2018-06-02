package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge2);
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
                    Intent i = new Intent(getApplicationContext(), LoadingActivity.class);
                    i.putExtra("PLAYER_ID", player.getPlayer());
                    i.putExtra("ROUND", player.getRound());
                    startActivity(i);
                }
            }
        });



    }
}

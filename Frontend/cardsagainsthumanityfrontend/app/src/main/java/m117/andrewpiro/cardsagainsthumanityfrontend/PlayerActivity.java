package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;
import android.content.Intent;

import cahCardParser.cardParser;

public class PlayerActivity extends AppCompatActivity {
    TextView playerIDDisplay;
    TextView blackCardQuestion;
    TextView noCardChosen;
    static int count = 1;
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


    /*
    protected void hardCode()
    {
        cardText[0] = "USC";
        cardText[1] = "UCLA";
    }
    */

    protected void update(){
        //randomly select one black card
        int bCardID = rand.nextInt(numBlackCards);
        Question = cp.getBlackCardByIndex(bCardID);

        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackCardQuestion);
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

        //hardCode();



        playerIDDisplay= (TextView) findViewById(R.id.playerID);
        playerIDDisplay.setText("Player " + count);

        //randomly select one black card
        int bCardID = rand.nextInt(numBlackCards);
        Question = cp.getBlackCardByIndex(bCardID);

        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackCardQuestion);
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
                    Intent i = new Intent(getApplicationContext(), LoadingActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}

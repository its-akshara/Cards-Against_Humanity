package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity {
    TextView playerIDDisplay;
    TextView blackCardQuestion;
    static int count = 1;
    final int CARD_NUMBER = 2;
    CharSequence Question = "What college do you go to?";
    TextView[] cards = new TextView[CARD_NUMBER];
    CharSequence[] cardText = new CharSequence[CARD_NUMBER];

    protected void hardCode()
    {
        cardText[0] = "USC";
        cardText[1] = "UCLA";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        hardCode();

        playerIDDisplay= (TextView) findViewById(R.id.playerID);
        playerIDDisplay.setText("Player " + count);

        //receive the info about the black card from a server. Use the global value
        blackCardQuestion = (TextView) findViewById(R.id.blackCardQuestion);
        blackCardQuestion.setText(Question);
        //need to display cards you can choose
//        for(int i = 0; i<CARD_NUMBER; i++)
//        {
//            cards[i] = (TextView) findViewById();
//        }
        cards[0] = (TextView) findViewById(R.id.card0);
        cards[1] = (TextView) findViewById(R.id.card1);

        //JSON parser stuff

        cards[0].setText(cardText[0]);
        cards[1].setText(cardText[1]);

        //set up on click listeners
        cards[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards[0].setText("You clicked me.");
            }
        });

        cards[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cards[1].setText("You clicked me.");
            }
        });
    }
}

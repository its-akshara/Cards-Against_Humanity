
package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import playerInformation.Player;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {
    Button startButton;
    Player player;
    TextView playerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        playerText = (TextView) findViewById(R.id.player_text);
        playerText.setText("You Chose:");

        //create or initialize connection here
        player  = new Player();
        player.setRound(0);

        final ImageView player1 = (ImageView) findViewById(R.id.player1);
        final ImageView player2 = (ImageView) findViewById(R.id.player2);
        final ImageView player3 = (ImageView) findViewById(R.id.player3);
        final ImageView player4 = (ImageView) findViewById(R.id.player4);

        player1.setAlpha(0.4f);
        player2.setAlpha(0.4f);
        player3.setAlpha(0.4f);
        player4.setAlpha(0.4f);

        player1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(0);
                player1.setAlpha(1.0f);
                player2.setAlpha(0.4f);
                player3.setAlpha(0.4f);
                player4.setAlpha(0.4f);

                playerText.setText("You Chose: Gene \"The Judge\" Block");
            }
        });
        player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(1);
                player1.setAlpha(0.4f);
                player2.setAlpha(1.0f);
                player3.setAlpha(0.4f);
                player4.setAlpha(0.4f);

                playerText.setText("You Chose: Prof. Smallberg");
            }
        });

        player3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(2);
                player1.setAlpha(0.4f);
                player2.setAlpha(0.4f);
                player3.setAlpha(1.0f);
                player4.setAlpha(0.4f);

                playerText.setText("You Chose: Prof. Eggert");
            }
        });
        player4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(3);
                player1.setAlpha(0.4f);
                player2.setAlpha(0.4f);
                player3.setAlpha(0.4f);
                player4.setAlpha(1.0f);

                playerText.setText("You Chose: Prof. Dzhanidze");
            }
        });

        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.isJudge())
                {
                    Intent i = new Intent(getApplicationContext(), JudgeActivity2.class);
                    i.putExtra("PLAYER_ID",player.getPlayer());
                    i.putExtra("ROUND",player.getRound());
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
                    i.putExtra("PLAYER_ID",player.getPlayer());
                    i.putExtra("ROUND",player.getRound());
                    startActivity(i);
                }
            }
        });
    }
}

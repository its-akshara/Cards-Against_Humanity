
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
        player1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(0);
                player1.setAlpha(1.0f);

                playerText.setText("You Chose: Gene Block");
            }
        });

        final ImageView player2 = (ImageView) findViewById(R.id.player2);
        player2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(1);
                player2.setAlpha(1.0f);

                playerText.setText("You Chose: Prof. Smallberg");
            }
        });


        final ImageView player3 = (ImageView) findViewById(R.id.player3);
        player3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(2);
                player3.setAlpha(1.0f);

                playerText.setText("You Chose: Prof. Eggert");
            }
        });

        final ImageView player4 = (ImageView) findViewById(R.id.player4);
        player4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.setPlayerID(3);
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

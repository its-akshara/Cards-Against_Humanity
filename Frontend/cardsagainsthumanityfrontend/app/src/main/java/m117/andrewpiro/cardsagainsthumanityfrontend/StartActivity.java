
package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import playerInformation.Player;

public class StartActivity extends AppCompatActivity {
    Button startButton;
    Player player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //create or initialize connection here
        player  = new Player();


        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.isJudge())
                {
                    Intent i = new Intent(getApplicationContext(), JudgeActivity2.class);
                    i.putExtra("PLAYER_ID",player.getPlayer());
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
                    i.putExtra("PLAYER_ID",player.getPlayer());
                    startActivity(i);
                }
            }
        });
    }
}

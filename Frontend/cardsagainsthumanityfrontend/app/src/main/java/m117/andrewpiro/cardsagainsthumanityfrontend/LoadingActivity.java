package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import playerInformation.Player;

public class LoadingActivity extends AppCompatActivity {
    Button nextRoundBtn;
    Player currentPlayer;
    Bundle previousActivityInfo;
    boolean winnerChosen = true;
    public GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        //API stuff
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
//                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(Nearby.CONNECTIONS_API)
//                .build();

        currentPlayer  = new Player();
        previousActivityInfo = getIntent().getExtras();
        currentPlayer.setRound(previousActivityInfo.getInt("ROUND"));
        currentPlayer.setPlayerID(previousActivityInfo.getInt("PLAYER_ID"));

        nextRoundBtn = (Button)findViewById(R.id.nextRoundButton);

        currentPlayer.updatePlayer();

        nextRoundBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                if(winnerChosen) {
                    if (currentPlayer.isJudge()) {
                        Intent i = new Intent(getApplicationContext(), JudgeActivity2.class);
                        i.putExtra("PLAYER_ID", currentPlayer.getPlayer());
                        i.putExtra("ROUND", currentPlayer.getRound());
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
                        i.putExtra("PLAYER_ID", currentPlayer.getPlayer());
                        i.putExtra("ROUND", currentPlayer.getRound());
                        startActivity(i);
                    }
                }
            }

        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}

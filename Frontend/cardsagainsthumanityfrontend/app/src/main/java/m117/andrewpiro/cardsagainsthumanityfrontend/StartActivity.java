
package m117.andrewpiro.cardsagainsthumanityfrontend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TEMPORARY isJudge, MODIFY LATER
                //***
                boolean isJudge = false;
                ///***
                Intent i;
                if(!isJudge){
                    i = new Intent(getApplicationContext(), PlayerActivity.class);
                }
                else {
                    i = new Intent(getApplicationContext(), JudgeActivity.class);
                }
                startActivity(i);
            }
        });
    }
}

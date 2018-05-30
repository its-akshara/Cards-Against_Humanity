package playerInformation;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.view.View;
//import android.support.v7.app.AppCompatActivity;
//import android.app.Activity;
//import android.os.Bundle;

public class Player {
    enum player_ids{JUDGE,PLAYER1,PLAYER2,PLAYER3};

    private int playerID;
    private String androidID;
    private int numberOfPlayers = 3;
    private int round = 0;

    public boolean isJudge()
    {
        return player_ids.JUDGE.equals(round%playerID);
    }

    public Player()
    {
       // TelephonyManager tm;
        //tm  = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        playerID = 1;
//        androidID = Settings.Secure.getString(, Settings.Secure.ANDROID_ID)
//        int tmpPlayerID = androidID.hashCode();
//        playerID = tmpPlayerID%numberOfPlayers;
    }

    public int getRound()
    {
        return round;
    }

    public int setRound(int n)
    {
        round = n;
    }

    public int getPlayer()
    {
        return playerID;
    }

    public void setPlayerID(int n)
    {
        playerID = n;
    }

    public void updatePlayer()
    {
//        if(playerID<2)
//            playerID++;
//        else
//            playerID = player_ids.JUDGE.ordinal();
        round++;
    }
}

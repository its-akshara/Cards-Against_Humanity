package playerInformation;

import android.provider.Settings;

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
        androidID =  Settings.Secure.ANDROID_ID;
        int tmpPlayerID = androidID.hashCode();
        playerID = tmpPlayerID%numberOfPlayers;
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

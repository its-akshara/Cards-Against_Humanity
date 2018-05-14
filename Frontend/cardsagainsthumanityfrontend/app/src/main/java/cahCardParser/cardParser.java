//package main.java;
package cahCardParser;
import android.content.Context;
import android.content.res.AssetManager;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class cardParser {
    private Context m_Context;
    private JSONObject m_jo;
    private JSONArray m_blackCards;
    private JSONArray m_whiteCards;
    private Map m_deckInfo;

    private int m_blackCardNum;
    private int m_whiteCardNum;

    public cardParser()
    {
        //JSONObject jo = (JSONObject) obj;

        /*AssetManager am = myContext.getAssets();
        //AssetFileDescriptor descriptor = myContext.getAssets().openFd("cards.json");

        try {
            InputStream ins = am.open("cards.json");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public boolean parse(String fullPathFileName)
    {
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(fullPathFileName));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        m_jo = (JSONObject) obj;

        m_blackCards = (JSONArray) m_jo.get("blackCards");
        m_whiteCards = (JSONArray) m_jo.get("whiteCards");
        m_deckInfo = ((Map)m_jo.get("90s"));

        m_blackCardNum = m_blackCards.size();
        m_whiteCardNum = m_whiteCards.size();

        return true;
    }

    public String getDeckName() // returns the deck name as a string
    {
        return (String) m_deckInfo.get("name");
    }

    public String getBlackCardsString()     // for testing: returns black cards separated by \n
    {
        String bCards = "";

        for (int i = 0; i < m_blackCardNum; i++)
        {
            JSONObject cardEntry = (JSONObject) m_blackCards.get(i);
            bCards += (String) cardEntry.get("text");
            bCards += "\n";
        }

        return bCards;
    }

    public String getWhiteCardsString()     // for testing: returns white cards separated by \n
    {
        String wCards = "";

        for(int i = 0; i < m_whiteCardNum; i++)
        {
            wCards += (String) m_whiteCards.get(i);
            wCards += "\n";
        }

        return wCards;
    }

    public int getNumberOfBlackCards()      // returns number of black cards in deck
    {
        return m_blackCardNum;
    }

    public int getNumberOfWhiteCards()      // returns number of white cards in deck
    {
        return m_whiteCardNum;
    }

    public String getBlackCardByIndex(int index)
    // takes index # and returns text of corresponding black card
    {
        if (index < 0)
            return "Error: index cannot be a negative value";
        else if (index >= m_blackCardNum)
            return "Error: index out of range";

        JSONObject bCard_temp = (JSONObject) m_blackCards.get(index);

        return (String) bCard_temp.get("text");
    }

    public String getWhiteCardByIndex(int index)
    // takes index # and returns text of corresponding white card
    {
        if (index < 0)
            return "Error: index cannot be a negative value";
        else if (index >= m_whiteCardNum)
            return "Error: index out of range";

        return (String) m_whiteCards.get(index);
    }

    /*private String loadJSONFromAsset(){
        String json = null;
        try{
            //AssetManager am
            InputStream is = myContext.getAssets.open("src/main/cards.json");
        }
    }*/
}

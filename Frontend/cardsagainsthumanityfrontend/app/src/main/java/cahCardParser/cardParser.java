/*
    JSONParser to retrieve JSON card information

    NOTE: JSON text is currently hardcoded into a member variable

    Summary (currently working methods):
        parse()
            imports JSON into variables, called by constructor
        getNumberOfBlackCards() - returns number of black cards in deck
        getNumberOfWhiteCards() - returns number of white cards in deck
        getBlackCardByIndex(int index)
            input: index #
            output: text of corresponding black card
        getWhiteCardByIndex(int index)
            input: index #
            output: text of corresponding white card
 */

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
import org.json.*;

public class cardParser {
    private Context m_Context;
    private JSONObject m_jo;
    private JSONArray m_blackCards;
    private JSONArray m_whiteCards;
    private Map m_deckInfo;

    private int m_blackCardNum;
    private int m_whiteCardNum;

    private final static String jsonStr = "{\"blackCards\":[{\"text\":\"Eggert has panned _ as \\\"poorly conceived\\\" and \\\"sloppily executed.\\\"\",\"pick\":1},{\"text\":\"Up next on Nickelodeon: \\\"Clarissa Explains _.\\\"\",\"pick\":1},{\"text\":\"Believe it or not, Jim Carrey can do a dead-on impression of _.\",\"pick\":1},{\"text\":\"It's Morphin' Time! Mastadon! Pterodactyl! Triceratops! Sabertooth Tiger! _!\",\"pick\":1},{\"text\":\"I'm a CS student at UCLA, I'm a lover, I'm a child, I'm _.\",\"pick\":1},{\"text\":\"How did Stella get her groove back after Project 3?\",\"pick\":1},{\"text\":\"Tonight on SNICK: \\\"Are You Afraid of _?\\\"\",\"pick\":1}],\"whiteCards\":[\"Several Michael Keatons.\",\"A bus that will explode if it goes under 50 miles per hour.\",\"Being bad at CS.\",\"Sunny D! Alright!\",\"Smallberg.\",\"Eggert's github, but for his bad projects.\",\"Switching to EE.\",\"The Y2K bug.\",\"Deregulating the mortgage market.\",\"Stabbing a Capri Sun.\",\"Wearing Nicolas Cage's acting career.\",\"Freeing prisoners.\",\"Kurt Cobain.\",\"The Great Carey Nachenberg.\",\"Liking big VIM and not being able to lie about it.\",\"Yelling \\\"girl power!\\\" and doing a high kick.\",\"Pure Moods, Vol. 1.\",\"Pizza in the morning, pizza in the evening, pizza at supper time.\",\"Reddit.\",\"Not getting an internship freshman year.\",\"Angels interfering in an otherwise fair baseball game.\",\"Cool 90s up-in-the-front hair.\",\"Project 3.\", \"Procrastinating on my CS M117 project.\", \"An easy A class.\", \"A $70 course reader.\",\"CS 33 with Eggert.\",\"Free fidget spinners from career fairs.\", \"My GPA.\",\"A crappy enrollment appointment.\", \"Getting lost in Boelter Hall.\"],\"UCLA\":{\"name\":\"CS M117 Pack\",\"black\":[0,1,2,3,4,5,6],\"white\":[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22],\"icon\":\"birthday-cake\"},\"order\":[\"UCLA\"]}";

    public cardParser() // current empty
    {
        //JSONObject jo = (JSONObject) obj;

        /*AssetManager am = myContext.getAssets();
        //AssetFileDescriptor descriptor = myContext.getAssets().openFd("cards.json");

        try {
            InputStream ins = am.open("cards.json");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        parse();
    }

    public boolean parse()
    {
        /*Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(jsonText));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        //m_jo = (JSONObject) obj;

        JSONParser parser = new JSONParser();

        try {
            m_jo = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

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
    private Context myContext;

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

        return true;
    }

    /*private String loadJSONFromAsset(){
        String json = null;
        try{
            //AssetManager am
            InputStream is = myContext.getAssets.open("src/main/cards.json");
        }
    }*/
}

import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.FileInputStream;
import java.io.InputStream;

public class cardParser {
    private Context myContext;

    public cardParser(Context context)
    {
        myContext = context;
        Object obj = new JSONParser().parse(new FileReader("cards.json"));

        AssetManager am = myContext.getAssets();
        InputStream ims = assetManager.open("cards.json");

        JSONObject jo = (JSONObject) obj;


    }

    private String loadJSONFromAsset(){
        String json = null;
        try{
            //AssetManager am
            InputStream is = myContext.getAssets.open("cards.json")
        }
    }
}

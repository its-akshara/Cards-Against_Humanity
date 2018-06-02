package CardsAgainstM117Application;

import android.app.Application;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import Interfaces.CardsAgainstM117Listener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class CardsAgainstM117Application extends Application {
    private static GoogleApiClient mGoogleApiClient;
    private static Context context;
    private static CardsAgainstM117Listener mListener;
    private boolean type = false;
    private static List<HashMap<String, String>> playersList;
    private static List<HashMap<String, String>> disconnectedPlayersList;
    private static final String TAG = "DixitApplication";

    private enum ConnectionType {
        Advertiser,
        Discoverer
    }

    ;
    private static GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.w(TAG, "onConnected");
            if(mListener != null){
                mListener.onConnected(bundle);
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d(TAG, "onConnectionSuspended");
            if(mListener != null){
                mListener.onConnectionSuspended(i);
            }
        }
    };

    private static GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.w(TAG, "onConnectionFailed");
            //default action
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initGoogleApiClient();
                }
            }, 2000);

            mListener.onConnectionFailed(connectionResult);
        }
    };

    private static ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(String discovererId, ConnectionInfo connectionInfo) {
            Log.w(TAG, "onConnectionInitiated discovererId: " + discovererId);
            if(mListener != null){
                mListener.onConnectionInitiated(discovererId, connectionInfo);
            }

        }

        @Override
        public void onConnectionResult(String discovererId, ConnectionResolution result) {
            Log.w(TAG, "onConnectionResult, result status: " + result.getStatus());
            if(mListener != null){
                mListener.onConnectionResult(discovererId, result);
            }
        }

        @Override
        public void onDisconnected(String discovererId) {
            Log.w(TAG, "onDisconnected, discoverer with id: " + discovererId + " disconnected");
            if(playersList != null && playersList.size() > 0) {
                for (int i = 0; i < playersList.size(); i++) {
                    String id = playersList.get(i).get("id");
                    String name = playersList.get(i).get("name");
                    if (id.equals(discovererId)) {
                        HashMap user = new HashMap();
                        user.put("id", id);
                        user.put("name", name);

                        disconnectedPlayersList.add(user);
                        playersList.remove(i);
                    }
                }
            }
            if(mListener != null){
                mListener.onDisconnected(discovererId);
            }
        }
    };

    private static PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(String s, Payload payload) {
            Log.w(TAG, "onPayloadReceived");
            mListener.onPayloadReceived(s, payload);
        }

        @Override
        public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {
            Log.w(TAG, "onPayloadTransferUpdate");
            mListener.onPayloadTransferUpdate(s, payloadTransferUpdate);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        context = this.getApplicationContext();
        if (mGoogleApiClient == null) {
            initGoogleApiClient();
          //do nothing
        }
        disconnectedPlayersList = new ArrayList<>();
    }

    public static void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(Nearby.CONNECTIONS_API)
                .setAccountName(context.getSharedPreferences("USER_DATA", 0).getString("USERNAME", ""))
                .build();
    }

    public static GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public static ConnectionLifecycleCallback getConnectionLifecycleCallback(){
        return mConnectionLifecycleCallback;
    }

    public static PayloadCallback getPayloadCallback(){
        return mPayloadCallback;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            //Nearby.Connections.stopAdvertising(mGoogleApiClient); is advertising or discovering, eject?
            mGoogleApiClient.disconnect();
        }
    }

    public static void setCardsAgainstM117Listener(CardsAgainstM117Listener listener){
        mListener = listener;
    }


    public static void saveConnectedUsers(List<HashMap<String, String>> playersListItems) {
        playersList = playersListItems;
    }


    public static List<HashMap<String, String>> getConnectedUsers() {
        return playersList;
    }

    public static List<HashMap<String, String>> getDisconnectedUsers() {
        return disconnectedPlayersList;
    }

}

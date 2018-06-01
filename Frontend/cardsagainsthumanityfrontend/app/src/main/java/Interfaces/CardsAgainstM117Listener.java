package Interfaces;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;


public interface CardsAgainstM117Listener {

    void onConnectionInitiated(String discovererId, ConnectionInfo connectionInfo);

    void onConnectionResult(String discovererId, ConnectionResolution result);

    void onDisconnected(String discovererId);

    void onConnected(@Nullable Bundle bundle);

    void onConnectionSuspended(int i);

    void onConnectionFailed(@NonNull ConnectionResult connectionResult);

    void onPayloadReceived(String s, Payload payload);

    void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate);
}

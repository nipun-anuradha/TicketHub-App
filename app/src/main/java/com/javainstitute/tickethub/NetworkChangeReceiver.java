package com.javainstitute.tickethub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                boolean noConnectivity = intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

                if (noConnectivity) {
                    // Internet or WiFi disconnected
                    Toast.makeText(context, "Internet disconnected", Toast.LENGTH_LONG).show();
                } else {
                    // Internet or WiFi connected
//                    Toast.makeText(context, "Internet connected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}

package com.crystall.smartlockprototype.services.firebase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.crystall.smartlockprototype.beans.firebase.Door;
import com.crystall.smartlockprototype.beans.firebase.User;
import com.crystall.smartlockprototype.config.Config;
import com.crystall.smartlockprototype.services.IDoorService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DoorService implements IDoorService{

    private DatabaseReference databaseReference;
    private WifiManager mainWifi;
    private Context mContext;

    public DoorService(Context mContext){
        initialize();
        this.mContext = mContext;
        mainWifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        connectToNetwork();
    }

    /**
     * This is for the original product - still not implemented.
     * @param doorId
     * @return
     */
    @Override
    public int unlock(String doorId, final String password, final User user) {

        getDoor(doorId, user, (door) -> {
            // Unlock signal to MCU. Use doorId and password for this.
        });

        return 0;
    }

    private boolean connectToNetwork() {
        if (!isMobileConnected()) {
            if (!mainWifi.isWifiEnabled()) {
                mainWifi.setWifiEnabled(true);
                System.out.println("WiFi enabled");
            }
            String networkSSID = "WiFi_Amila";
            String networkPass = "Oshada@2014";
            boolean returnVal = false;
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";
            conf.preSharedKey = "\"" + networkPass + "\"";
            mainWifi.addNetwork(conf);
            List<WifiConfiguration> list = mainWifi.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    mainWifi.disconnect();
                    mainWifi.enableNetwork(i.networkId, true);
                    mainWifi.reconnect();
                    returnVal = true;
                    break;
                }
            }
            return returnVal;

        }else{
            Toast.makeText(mContext,"Please Turn Off Mobile Network to Proceed",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean isMobileConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        return isMobileConn;
    }


    /**
     * This is for the Prototype - have to implement
     * @return
     */
    @Override
    public int unlock() {
//        TODO: IMPLEMENT THE NODEMCU LOGIC HERE.
        if(true){
            System.out.println("Connected");
            HTTPRequestService service = new HTTPRequestService(mContext);
            service.lockRequest();
        }else{
            Toast.makeText(mContext,"Error when connecting to the Lock",Toast.LENGTH_LONG).show();
        }

        return 0;
    }

    /**
     * Add doors to the database
     * @param door
     * @return
     */
    @Override
    public int add(Door door, User user, final Context context) {
        Task<Void> task = getDatabaseReference().child("users").child(user.getUsername()).child("doors").setValue(door)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Door Successfully added!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        if(task.isSuccessful()) return 1;
        return 0;
    }

    /**
     * Get a door from the database
     * @param doorId
     * @param user
     * @param callback
     * @return
     */
    @Override
    public Door getDoor(String doorId, User user, FirebaseDoorCallback callback) {
        getDatabaseReference().child("users").child(user.getUsername()).child("doors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Door door = dataSnapshot.getValue(Door.class);
                        if(door == null) return;
                        callback.onCallback(door);
                        Log.i("RETRIEVED_DOOR", door.toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("RETRIEVED_USER", "Door retrieval failed.");
                    }
                });
        return null;
    }

    /**
     * Initialize the firebase connection
     * @return
     */
    public DatabaseReference initialize() {
        this.databaseReference = FirebaseDatabase
                .getInstance(Config.FIREBASE_URL)
                .getReference();
        if (this.databaseReference.getDatabase() == null) {
            return null;
        } else {
            Log.i("CONNECTION", "Firebase Connection Successfully Granted for Door Service.");
        }
        return databaseReference;
    }

    private DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}

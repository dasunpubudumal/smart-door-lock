package com.crystall.smartlockprototype;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crystall.smartlockprototype.beans.firebase.User;
import com.crystall.smartlockprototype.services.WiFiConnectivityService;
import com.crystall.smartlockprototype.services.firebase.AuthenticationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Key extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button submit;
    private Button btn;
    private boolean accept = false;
    private AuthenticationService authenticationService;
    private WiFiConnectivityService connectivityService;
    private WifiManager wifiManager;

    /**
     * @param savedInstanceState
     */
    @SuppressLint("WifiManagerLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);

        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        submit = findViewById(R.id.btnLogIn);
        btn = findViewById(R.id.btn);
        submit.setOnClickListener(this::password_submit);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        btn.setOnClickListener(v -> {
            wifiManager.setWifiEnabled(true);
        });


        try {
            authenticationService = new AuthenticationService();
        } catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED!");
        }
        registerReceiver(new MyBroadcastReceiver("DasunPubudumal", "birthday19940730"), new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    /**
     * @param v
     * @return
     */
    @SuppressLint("SetTextI18n")
    public void password_submit(View v) {
        try {
            if (password.length() > 0 && username.length() > 0) {
                authenticationService.login(username.getText().toString(),
                        getApplicationContext(),
                        password.getText().toString()
                );
            } else {
                Toast.makeText(getApplicationContext(), "Please enter proper credentials!",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("ERROR", "Message: " + e.getMessage());
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        private String ssid;
        private String pass;
        private final WifiConfiguration conf = new WifiConfiguration();

        private MyBroadcastReceiver(String ssid, String pass) {
            this.ssid = ssid;
            this.pass = pass;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            StringBuffer stringBuffer = new StringBuffer();
//            List<ScanResult> scanResults = wifiManager.getScanResults();
//            for (ScanResult scanResult:scanResults) {
//                stringBuffer.append(scanResult);
//            }
            conf.SSID =  "\"\"" + ssid +  "\"\"";
            conf.preSharedKey =  "\"\""+ pass + "\"\"";
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiManager.addNetwork(conf);
            System.out.println("HERE COMES SCANNED RESULTS");
            System.out.println(stringBuffer);
        }
    }

    private void sendRequest(String ssid, String pass) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + ssid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                Toast.makeText(this, "Request Completed!", Toast.LENGTH_LONG).show();
            }, error -> {
                Toast.makeText(this, "Request Failed!", Toast.LENGTH_SHORT).show();
        });

        queue.add(stringRequest);
    }

    private WifiConfiguration config(String ssid, String pass) {
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID =  "\"\"" + ssid +  "\"\"";
        conf.preSharedKey =  "\"\""+ pass + "\"\"";
        conf.status = WifiConfiguration.Status.ENABLED;
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        return conf;
    }

}


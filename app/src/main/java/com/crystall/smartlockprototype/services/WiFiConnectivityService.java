package com.crystall.smartlockprototype.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

public class WiFiConnectivityService extends BroadcastReceiver{

    private WifiManager wifiManager;
    private Context context;
    private static final String ACTION_WIFI_ON = "android.intent.action.WIFI_ON";
    private static final String ACTION_WIFI_OFF = "android.intent.action.WIFI_OFF";
    private static final String ACTION_CONNECT_TO_WIFI = "android.intent.action.CONNECT_TO_WIFI";
    private String ssid;
    private String pass;

    public WiFiConnectivityService(Context context) {
        this.context = context;
    }

    public WiFiConnectivityService(String ssid, String pass, Context context){
        this.ssid = ssid;
        this.pass = pass;
    }

    public void connect(String ssid, String pass, Context context) {

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
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

        int i1 = wifiManager.addNetwork(conf);
        System.out.println("i1 is: " + i1);

        if(i1 >= 0) {
            List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i: configuredNetworks) {
                if(i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    Log.i("WIFI_CONNECTIVITY", "Successfully connected to " + ssid);
                    break;
                }
            }
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final String action = intent.getAction();

        if(action != null) {
            switch (action) {
                case ACTION_WIFI_ON:
                    wifiManager.setWifiEnabled(true);
                    break;
                case ACTION_WIFI_OFF:
                    wifiManager.setWifiEnabled(false);
                    break;
                case ACTION_CONNECT_TO_WIFI:
                    final String networkSSID = getSsid();
                    final String networkPass = getPass();

                    if(networkSSID != null || networkPass != null){
                        connect(networkSSID, networkPass, context);
                    }
            }
        }
    }

    public static boolean setSsidAndPassword(Context context, String ssid, String ssidPassword) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(context.WIFI_SERVICE);
            Method getConfigMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            WifiConfiguration wifiConfig = (WifiConfiguration) getConfigMethod.invoke(wifiManager);

            wifiConfig.SSID = ssid;
            wifiConfig.preSharedKey = ssidPassword;

            Method setConfigMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            setConfigMethod.invoke(wifiManager, wifiConfig);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getSsid() {
        return ssid;
    }

    private void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

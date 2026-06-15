package com.vipwapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;
import org.json.JSONObject;

public class VIPWappBridge {
    private final Context context;
    private final SharedPreferences prefs;

    public VIPWappBridge(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences("vipwapp_settings", Context.MODE_PRIVATE);
    }

    @JavascriptInterface
    public void saveSetting(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    @JavascriptInterface
    public boolean getSetting(String key) {
        return prefs.getBoolean(key, false);
    }

    @JavascriptInterface
    public String getAllSettings() {
        try {
            JSONObject obj = new JSONObject();
            for (java.util.Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
                obj.put(entry.getKey(), entry.getValue());
            }
            return obj.toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    @JavascriptInterface
    public String getModuleStatus() {
        // Check if Zygisk module is active
        boolean moduleActive = false;
        try {
            java.io.File f = new java.io.File("/data/adb/modules/vipwapp");
            moduleActive = f.exists();
        } catch (Exception ignored) {}

        // Check WhatsApp
        boolean waInstalled = false;
        try {
            context.getPackageManager().getPackageInfo("com.whatsapp", 0);
            waInstalled = true;
        } catch (Exception ignored) {}

        boolean wabInstalled = false;
        try {
            context.getPackageManager().getPackageInfo("com.whatsapp.w4b", 0);
            wabInstalled = true;
        } catch (Exception ignored) {}

        try {
            JSONObject status = new JSONObject();
            status.put("moduleActive", moduleActive);
            status.put("waInstalled", waInstalled);
            status.put("wabInstalled", wabInstalled);
            status.put("appVersion", "1.0.0");
            return status.toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    @JavascriptInterface
    public void saveColorSetting(String key, String colorHex) {
        prefs.edit().putString(key, colorHex).apply();
    }

    @JavascriptInterface
    public String getColorSetting(String key, String defaultColor) {
        return prefs.getString(key, defaultColor);
    }
}

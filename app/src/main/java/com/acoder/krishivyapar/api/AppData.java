package com.acoder.krishivyapar.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class AppData {

    private final SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "zombiePref3";

    private final Context context;

    public AppData(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /*public VendorApp(@NonNull Context context) {
    }*/

    public Context getContext() {
        return context;
    }

    public void updateName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.apply();
    }

    public void updateEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    public void updateMobile(String mobile) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.apply();
    }

    public String getName() {
        return sharedPreferences.getString("name", null);
    }
    public String getEmail() {
        return sharedPreferences.getString("email", null);
    }
    public String getMobile() {
        return sharedPreferences.getString("mobile", null);
    }
    public String getProfileURL() {
        return sharedPreferences.getString("profileURL", null);
    }

//    saves the user credentials in app privately. Can be used later.
    public void loginLocally(String mobile, String password){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.putString("password", password);
        editor.apply();
    }

    public boolean hasCurrentUser(){
        return getUserEmail() != null;
    }

    public String getUserEmail() {
        return sharedPreferences.getString("mobile", null);
    }
    public String getUserPassword() {
        return sharedPreferences.getString("password", null);
    }

    public Map<String, String> getCredentials(){
        Map<String, String> map = new HashMap<>();
        map.put("user_email", getUserEmail());
        map.put("user_password", sharedPreferences.getString("password", null));
        return map;
    }



    public void updateProfileURL(String profileURL) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileURL", profileURL);
        editor.apply();
    }

    public float getFreeDeliveryOver() {
        return sharedPreferences.getFloat("free_delivery_over", 0f);
    }

    public float getDeliveryCharge() {
        return sharedPreferences.getFloat("delivery_charge", 0f);
    }

    public float getMinimumTotalAmount() {
        return sharedPreferences.getFloat("minimum_total_amount", 0f);
    }

    public void putSettings(float free_delivery_over, float delivery_charge, float minimum_total_amount, String title, String description, String owner_name, String address, String lat, String lng, String email, String website, String phone, String whatsApp, int deliveryRadius) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("free_delivery_over", free_delivery_over);
        editor.putFloat("delivery_charge", delivery_charge);
        editor.putFloat("minimum_total_amount", minimum_total_amount);
        editor.putString("store_title", title);
        editor.putString("description", description);
        editor.putString("store_owner_name", owner_name);
        editor.putString("address", address);
        editor.putString("store_lat", lat);
        editor.putString("store_lng", lng);
        editor.putString("email", email);
        editor.putString("website", website);
        editor.putInt("deliveryRadius", deliveryRadius);
        editor.putString("phone", phone);
        editor.putString("whatsApp", whatsApp);
        editor.apply();
    }

    public Map<String, String> getSettings() {
        Map<String, String> map = new HashMap<>();
        map.put("free_delivery_over", String.valueOf(sharedPreferences.getFloat("free_delivery_over", -1)));
        map.put("delivery_charge", String.valueOf(sharedPreferences.getFloat("delivery_charge", -1)));
        map.put("minimum_total_amount", String.valueOf(sharedPreferences.getFloat("minimum_total_amount", -1)));
        map.put("store_title", sharedPreferences.getString("store_title", ""));
        map.put("description", sharedPreferences.getString("description", ""));
        map.put("store_owner_name", sharedPreferences.getString("store_owner_name", ""));
        map.put("address", sharedPreferences.getString("address", ""));
        map.put("store_lat", sharedPreferences.getString("store_lat", "0"));
        map.put("store_lng", sharedPreferences.getString("store_lng", "0"));
        map.put("email", sharedPreferences.getString("email", ""));
        map.put("website", sharedPreferences.getString("website", ""));
        map.put("deliveryRadius", String.valueOf(sharedPreferences.getInt("deliveryRadius", 0)));
        map.put("phone", sharedPreferences.getString("phone", ""));
        map.put("whatsApp", sharedPreferences.getString("whatsApp", ""));
        return map;
    }

    public void setBaseURL(String url) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("urlOnBase", url);
        editor.apply();
    }

    public String getBaseURL(){
        return sharedPreferences.getString("urlOnBase", "");
    }
}

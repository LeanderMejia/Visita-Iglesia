package com.project.visitatarlac.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.project.visitatarlac.Model.ChurchMapModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HelperClass extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    public void setFragment(FragmentManager fragmentManager, int frameLayoutView, Fragment fragment, String backstack) {
        fragmentManager.beginTransaction().replace(frameLayoutView, fragment).addToBackStack(backstack).commit();
    }

    public Bundle createBundle(String key1, String passData1, String key2, String passData2) {
        Bundle bundle = new Bundle();
        bundle.putString(key1, passData1);
        if (key2 != null && passData2 != null) {
            bundle.putString(key2, passData2);
        }
        return bundle;
    }

    public Bundle createChurchInfoBundle(String churchName, String churchAddress, String churchDescription, String churchImg, ArrayList<String> churchInfo) {
        Bundle bundle = new Bundle();
        bundle.putString("CHURCH_NAME", churchName);
        bundle.putString("CHURCH_ADDRESS", churchAddress);
        bundle.putString("CHURCH_DESCRIPTION", churchDescription);
        bundle.putString("CHURCH_IMG", churchImg);
        bundle.putStringArrayList("CHURCH_INFO", churchInfo);
        return bundle;
    }

    public Bundle createPrayerBundle(String prayerName, String prayerDescription, String prayerImg) {
        Bundle bundle = new Bundle();
        bundle.putString("PRAYER_NAME", prayerName);
        bundle.putString("PRAYER_DESCRIPTION", prayerDescription);
        bundle.putString("PRAYER_IMG", prayerImg);
        return bundle;
    }

    public Bundle createChurchMarkerBundle(String key1, double latitude, String key2, double longitude, String key3, String churchName, String key4, String churchAddress, String key5, String churchDescription, String key6, String churchImg, String key7, ArrayList<String> churchInfo) {
        Bundle bundle = new Bundle();
        bundle.putDouble(key1, latitude);
        bundle.putDouble(key2, longitude);
        bundle.putString(key3, churchName);
        bundle.putString(key4, churchAddress);
        bundle.putString(key5, churchDescription);
        bundle.putString(key6, churchImg);
        bundle.putStringArrayList(key7, churchInfo);
        return bundle;
    }

    public JSONArray getJSONArray(Context context, String jsonArrayName) {
        JSONArray jsonArray;
        try {
            InputStream inputStream = context.getAssets().open("OfflineData.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray(jsonArrayName);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonArray;
    }
}

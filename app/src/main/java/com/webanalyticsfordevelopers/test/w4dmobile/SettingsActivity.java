package com.webanalyticsfordevelopers.test.w4dmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.adobe.marketing.mobile.MobileCore;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // create contextData map
        Map<String,String> contextData = new HashMap<>();
        // add language = en
        contextData.put("language", "en");
        MobileCore.trackState("Settings", contextData);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobileCore.setApplication(getApplication());
        MobileCore.lifecycleStart(null);
    }

    @Override
    public void onPause() {
        MobileCore.lifecyclePause();
        super.onPause();
    }

}

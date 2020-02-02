package com.webanalyticsfordevelopers.test.w4dmobile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.Target;
import com.adobe.marketing.mobile.TargetRequest;
import com.adobe.marketing.mobile.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileCore.setApplication(this.getApplication());
        MobileCore.setLogLevel(LoggingMode.DEBUG);

        try {
            Target.registerExtension();
            UserProfile.registerExtension();
            Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            MobileCore.start(new AdobeCallback() {
                @Override
                public void call(Object o) {
                    MobileCore.configureWithAppID("3028746f70eb/d6b1ea7ebece/launch-e736742547f3");
                }
            });
        } catch (InvalidInitException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Creating Target request...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                TargetRequest targetRequest1 = new TargetRequest("w4dmobile-targeting1", null
                        , "{\"text\":\"default\",\"url\":\"https://webanalyticsfordevelopers.com/\""
                        , new AdobeCallback<String>() {
                    @Override
                    public void call(String jsonResponse) {
                        Snackbar.make(view, "Content received", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        // so this should be JSON content...
                        try {
                            JSONObject targetJSONResponse = new JSONObject(jsonResponse);
                            // replace content as needed
                            final String textForTextView = targetJSONResponse.getString("text");
                            final TextView textView1 = findViewById(R.id.textView1);
                            final String urlForWebViewAsText = targetJSONResponse.getString("url");
                            URL url = new URL(urlForWebViewAsText); // I like to check my URLs
                            final WebView webView = findViewById(R.id.webView);
                            if (urlForWebViewAsText.length() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView1.setText(textForTextView);
                                        webView.loadUrl(urlForWebViewAsText);
                                        Snackbar.make(view, "Content replaced", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Snackbar.make(view, "Content from Target not valid JSON", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } catch (MalformedURLException e) {
                            Snackbar.make(view, "Target returned invalid URL", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
                List<TargetRequest> requests = new ArrayList<>();
                requests.add(targetRequest1);
                // prep done, now retrieve content
                Snackbar.make(view, "Retrieving content from Target", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Target.retrieveLocationContent(requests, null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

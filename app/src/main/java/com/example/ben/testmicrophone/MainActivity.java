package com.example.ben.testmicrophone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;

import com.google.gson.JsonElement;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements ApiAiShimListener {

    private ApiAiShim nlp;
    private StringSpeaker voice;
    private PermissionRequester permRequester;
    private static final String clientToken = "40e5fdf2196740ea9d6f22ae2990f29f";
    private SearchAndPlay searchAndPlay = new SearchAndPlay(this);

    private TextView resultTextView;

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView)findViewById(R.id.editTextView);

        nlp = new ApiAiShim(this, clientToken, this);
        voice = new StringSpeaker(this);
        permRequester = new PermissionRequester(this);
        permRequester.request(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permRequester.storeResult(requestCode, permissions[0], grantResults);
    }

    public void startRecognition(final View view){
      nlp.startRecognition();
    }

    public void stopRecognition(final View view){
      nlp.stopRecognition();
    }

    public void ApiAiError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error);
            }
        });
    }

    public void ApiAiResult(String speech, String intent, HashMap<String, JsonElement> params) {

        resultTextView.setText(speech);
        voice.pronounce(speech);


        // Just for debugging.
        if (params != null && !params.isEmpty()) {
            Log.i(TAG, "Parameters: ");
            for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
            }
        }

        IntentHandler handler = IntentHandlerFactory.createHandler(intent, params);
        if(handler != null)
            handler.handle(params);
    }
}

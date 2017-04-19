package com.example.ben.testmicrophone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;


public class MainActivity extends AppCompatActivity implements ApiAiShimListener {

    private ApiAiShim nlp;
    private TextView resultTextView;
    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 201;

    private TextToSpeech tts;

    public static final String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView)findViewById(R.id.editTextView);

        nlp = new ApiAiShim(this, this);

        initTextToSpeech();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void initTextToSpeech() {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

  public void startRecognition(final View view){
      nlp.startRecognition();
  }

  public void stopRecognition(final View view){
      nlp.stopRecognition();
    }

    @Override
    public void ApiAiError(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error);
            }
        });
    }

    @Override
    public void ApiAiResult(String speech, String intent, HashMap<String, JsonElement> params) {

        resultTextView.setText(speech);
        tts.speak(speech, QUEUE_ADD, null, "7");

        if (params != null && !params.isEmpty()) {
            Log.i(TAG, "Parameters: ");
            for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
            }
        }

    }
}

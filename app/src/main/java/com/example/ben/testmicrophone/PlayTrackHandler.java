package com.example.ben.testmicrophone;

import com.google.gson.JsonElement;
import java.util.HashMap;

/**
 * Created by dave on 5/1/17.
 */

public class PlayTrackHandler implements IntentHandler {

    PlayTrackHandler() {
    }

    @Override
    public void handle(HashMap<String, JsonElement> params) {
        JsonElement element = params.get("track");
        String query = element.getAsString();

    }
}

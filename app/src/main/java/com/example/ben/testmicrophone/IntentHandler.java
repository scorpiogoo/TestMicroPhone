package com.example.ben.testmicrophone;

import com.google.gson.JsonElement;

import java.util.HashMap;

/**
 * Created by dave on 5/1/17.
 */

public interface IntentHandler {
    void handle(HashMap<String, JsonElement> params);
}


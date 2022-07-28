package com.damavandit.apps.chair.other;

import android.content.Intent;

import com.damavandit.apps.chair.MainActivity;

import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

public class PusheReceiver extends PusheListenerService {

    @Override
    public void onMessageReceived(JSONObject json, JSONObject messageContent) {
        if (json.length() == 0) {
            return;
        } else {
            getApplicationContext().startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }
}

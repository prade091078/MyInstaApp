package com.prade.parsestarterapplication;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class ParseStarterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId(getString(R.string.parseAppId))
        .clientKey(getString(R.string.parseAppClientId))
        .server(getString(R.string.parseAppServerId))
        .build()
        );

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }
}

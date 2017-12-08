package com.example.maruta.instalogin;

import android.app.Application;
import android.util.Log;
import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("0ac770d9a824cb69b0d81c1f7bfdad522e72ff66")
                .clientKey("6235c393b0d2e256b59bf8a6e18b22f11a0be05c")
                .server("http://13.58.12.116:80/parse/")
                .build()
        );

        ParseUser.enableAutomaticUser();

        ParseACL defAcl = new ParseACL();
        defAcl.setPublicReadAccess(true);
        defAcl.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defAcl, true);

    }
}

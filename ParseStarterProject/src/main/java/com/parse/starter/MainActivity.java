/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {


    public void redirectActivity() {
        // if user is logged in as sos then send to the SOS Activity
        if (ParseUser.getCurrentUser().getString("responderOrSos").equals("sos")) {

            Intent intent = new Intent(getApplicationContext(), SOSActivity.class);
            startActivity(intent);

        } else {
            // if user is logged in as responder they will go to the View Sos Activity
            Intent intent = new Intent(getApplicationContext(), ViewSosActivity.class);
            startActivity(intent);


        }
    }



    public void getStarted(View view) {
        System.out.println("Get Started 1"); // Had issue with log in used the printIn to see where it was crashing
        Switch userTypeSwitch = (Switch) findViewById(R.id.userTypeSwitch);
        System.out.println("Get Started 2");
        Log.i("Switch value", String.valueOf(userTypeSwitch.isChecked()));
        System.out.println("Get Started 3");
        String userType = "sos";
        System.out.println("Get Started 4");
        System.out.println("Get Started 4: " + userTypeSwitch.isChecked());
        if (userTypeSwitch.isChecked()) {
            System.out.println("Get Started if usertype is clicked");
            userType = "responder";

        }
        System.out.println("Get Started 5");
        System.out.println("Get Started 5 " + ParseUser.getCurrentUser());
        ParseUser.getCurrentUser().put("responderOrSos", userType);
        System.out.println("Get Started 6");
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                System.out.println("Get Started 7");
                redirectActivity();

            }
        });




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("On create 1");
        getSupportActionBar().hide(); // Hiding the action bar

        System.out.println("On create 2");

        // If the user is not logged in then they will be anonymously logged in
        if (ParseUser.getCurrentUser() == null) {
            System.out.println("On create: getCurrentUser() == null");
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    System.out.println("On create ParseAnonymousUtils.logIn");
                    if (e == null) {
                        System.out.println("On create: e = null");
                        Log.i("Info", "Anonymous login successful"); // Checking the log to see if the user was logged in successfully

                    } else {
                        System.out.println("On create: anon failed"); // Alert me in the log if login was unsuccessful
                        Log.i("Info", "Anonymous login failed");

                    }
                }
            });

        } else {
                // if responder or SOS field is not blank then redirect to the responder or Sos activity
            if (ParseUser.getCurrentUser().get("responderOrSos") != null) {

                Log.i("Info", "Redirecting as " + ParseUser.getCurrentUser().get("responderOrSos"));

                redirectActivity();

            }


        }

        // tracking the application in the background
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}
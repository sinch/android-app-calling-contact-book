package com.sinch.apptoappcontactbook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginActivity extends Activity {

    private Button signUpButton;
    private Button loginButton;
    private EditText usernameField;
    private EditText passwordField;
    private String username;
    private String password;
    private Intent notVerifiedIntent;
    private Intent verifiedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notVerifiedIntent = new Intent(this, VerifyPhoneNumberActivity.class);
        verifiedIntent = new Intent(this, ListContactsActivity.class);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            if (currentUser.get("phoneNumber") != null) {
                startActivity(verifiedIntent);
            } else {
                startActivity(notVerifiedIntent);
            }
        }

        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        signUpButton = (Button) findViewById(R.id.signupButton);
        usernameField = (EditText) findViewById(R.id.loginUsername);
        passwordField = (EditText) findViewById(R.id.loginPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(ParseUser user, com.parse.ParseException e) {
                        if (user != null) {
                            if (user.get("phoneNumber") != null) {
                                startActivity(verifiedIntent);
                            } else {
                                startActivity(notVerifiedIntent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                "Wrong username/password combo",
                                Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = usernameField.getText().toString();
                password = passwordField.getText().toString();

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);

                user.signUpInBackground(new SignUpCallback() {
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            startActivity(notVerifiedIntent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                "There was an error signing up."
                                , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}

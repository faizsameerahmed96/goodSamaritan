package com.green.goodsamaritan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_login_al).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.showLoading(Login.this, true);
                //TODO Check if the fields are empty

                String email = ((EditText) findViewById(R.id.et_email_al)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.et_password_al)).getText().toString().trim();

                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        //TODO Cahnge the toast messages
                        ApplicationUtils.showLoading(Login.this, false);
                        if(e==null){
                            Toast.makeText(Login.this, "User signed up !" , Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this, MainActivity.class).
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }else{
                            Toast.makeText(Login.this, "User signed up failed." , Toast.LENGTH_LONG).show();
                            Log.e("message", e.getMessage());
                        }
                    }
                });
            }
        });
    }
}

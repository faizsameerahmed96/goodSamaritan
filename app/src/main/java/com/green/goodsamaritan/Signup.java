package com.green.goodsamaritan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViewById(R.id.btn_login_as).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });
        findViewById(R.id.btn_signup_as).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationUtils.showLoading(Signup.this, true);
                ParseUser user = new ParseUser();
                user.put("name", ((EditText) findViewById(R.id.et_name_as)).getText().toString().trim());
                user.setUsername(((EditText) findViewById(R.id.et_email_as)).getText().toString().trim());
                user.setPassword(((EditText) findViewById(R.id.et_password_as)).getText().toString().trim());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        ApplicationUtils.showLoading(Signup.this, false);
                        if(e== null){
                            Toast.makeText(Signup.this, "User signed up !" , Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Signup.this, MainActivity.class).
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }else{
                            Toast.makeText(Signup.this, "User signed up failed." , Toast.LENGTH_LONG).show();
                            Log.e("message", e.getMessage());
                        }
                    }
                });
            }
        });
    }

    //TODO check if the fields are empty
}

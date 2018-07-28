package com.crystall.smartlockprototype;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crystall.smartlockprototype.services.firebase.AuthenticationService;

public class Key extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button submit;
    private boolean accept  = false;
    private AuthenticationService authenticationService;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);

        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        submit = findViewById(R.id.btnLogIn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_submit(v);
            }
        });

        try {
            authenticationService = new AuthenticationService();
        } catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED!");
        }
    }

    /**
     *
     * @param v
     * @return
     */
    @SuppressLint("SetTextI18n")
    public void password_submit(View v){
        try {
            if(password.length() > 0 && username.length() > 0) {
                authenticationService.login(username.getText().toString(),
                        getApplicationContext(),
                        password.getText().toString()
                );
            } else {
                Toast.makeText(getApplicationContext(), "Please enter proper credentials!" ,
                        Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e){
            Log.e("ERROR", "Message: "+ e.getMessage());
        }
    }


}

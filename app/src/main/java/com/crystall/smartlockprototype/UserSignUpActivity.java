package com.crystall.smartlockprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crystall.smartlockprototype.beans.firebase.User;
import com.crystall.smartlockprototype.services.firebase.AuthenticationService;
import com.crystall.smartlockprototype.services.firebase.FirebaseAuthService;
import com.crystall.smartlockprototype.util.TimestampParser;

public class UserSignUpActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button signUp;
    private Button firebaseSignUp;

    private FirebaseAuthService firebaseAuthService;
    private AuthenticationService authenticationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        username = findViewById(R.id.txtSignUpUsername);
        password = findViewById(R.id.txtDoorPassword);
        signUp = findViewById(R.id.btnSignUp);
        firebaseSignUp = findViewById(R.id.btnSignUpFirebase);
        firebaseSignUp.setOnClickListener(this::setFirebaseSignUp);
        signUp.setOnClickListener(this::setSignUp);

        firebaseAuthService = new FirebaseAuthService();

    }

    private void setFirebaseSignUp(View v) {
        if(username.length() >0 && password.length() > 0) firebaseAuthService.signUp(username.toString(),
                password.toString(), getApplicationContext());
        else  Toast.makeText(getApplicationContext(), "Please enter credentials!",
                Toast.LENGTH_SHORT).show();

    }

    private void setSignUp(View v) {
        if(username.length() >0 && password.length() > 0) authenticationService.write(new User(
                username.toString(),
                password.toString(),
                new TimestampParser().storeTimeStamp(),
                null));
        else  Toast.makeText(getApplicationContext(), "Please enter credentials!",
                Toast.LENGTH_SHORT).show();

    }

}

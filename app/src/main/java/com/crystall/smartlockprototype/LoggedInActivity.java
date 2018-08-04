package com.crystall.smartlockprototype;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crystall.smartlockprototype.beans.firebase.User;
import com.crystall.smartlockprototype.services.firebase.DoorService;
import com.google.firebase.auth.FirebaseUser;

public class LoggedInActivity extends AppCompatActivity {

    private TextView label;
    private User currentUser;
    private Button addDoors;
    private DoorService doorService;
    private FirebaseUser firebaseUser;
    private Button signUp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // Set up the component IDs.
        label = findViewById(R.id.txtLoggedIn);
        addDoors = findViewById(R.id.btnAddDoors);
        signUp = findViewById(R.id.btnSignUp);
        addDoors.setOnClickListener(this::add);
        signUp.setOnClickListener(this::startUserSignUpActivity);

        // Set up the global user.
        Intent i = getIntent();

        // TODO: Two classes of users. Have to use if conditions for everyting regarding to the two classes of users.
        if(i.getSerializableExtra("USER") instanceof User) {
            setCurrentUser((User) i.getSerializableExtra("USER"));
        } else if (i.getSerializableExtra("USER") instanceof FirebaseUser) {
            setCurrentUser((FirebaseUser) i.getSerializableExtra("USER"));
        } else {
            Toast.makeText(getApplicationContext(), "Invalid Serialization",
                    Toast.LENGTH_SHORT).show();
        }

        // Display the Headline.
        label.setText("Welcome, " + firstLetterUppercase(getCurrentUser().getUsername()));

        doorService = new DoorService();
    }

    public void add(View v) {
        doorService.unlock();
    }

    private User getCurrentUser() {
        return currentUser;
    }

    private void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private void setCurrentUser(FirebaseUser user) {this.firebaseUser = user;}

    private FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    private String firstLetterUppercase(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }


    private void startUserSignUpActivity(View v){
        getApplicationContext().startActivity(new Intent(getApplicationContext(), UserSignUpActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}

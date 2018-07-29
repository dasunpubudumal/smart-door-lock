package com.crystall.smartlockprototype;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crystall.smartlockprototype.beans.firebase.User;

public class LoggedInActivity extends AppCompatActivity {

    private TextView label;
    private User currentUser;
    private Button addDoors;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // Set up the component IDs.
        label = findViewById(R.id.txtLoggedIn);
        addDoors = findViewById(R.id.btnAddDoors);

        // Set up the global user.
        Intent i = getIntent();
        setCurrentUser((User) i.getSerializableExtra("USER"));

        // Display the Headline.
        label.setText("Welcome, " + getCurrentUser().getUsername().substring(0,1).toUpperCase()
        + getCurrentUser().getUsername().substring(1));

        addDoors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { add(v); }
        });
    }

    public void add(View v) {
        // TODO: IMPLEMENT THE NODEMCU LOGIC HERE.
    }

    private User getCurrentUser() {
        return currentUser;
    }

    private void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}

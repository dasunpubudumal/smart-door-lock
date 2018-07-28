package com.crystall.smartlockprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DoorsActivity extends AppCompatActivity {

    private EditText txtSSID;
    private EditText txtDoorPassword;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doors);

        txtSSID = findViewById(R.id.txtDoorSSID);
        txtDoorPassword = findViewById(R.id.txtDoorPassword);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { add(v); }
        });
    }

    /**
     * Add a door.
     * @param v
     */
    private void add(View v) {
    }
}

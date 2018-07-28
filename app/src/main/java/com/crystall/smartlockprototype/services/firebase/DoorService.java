package com.crystall.smartlockprototype.services.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.crystall.smartlockprototype.beans.firebase.Door;
import com.crystall.smartlockprototype.beans.firebase.User;
import com.crystall.smartlockprototype.config.Config;
import com.crystall.smartlockprototype.services.IDoorService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoorService implements IDoorService{

    private DatabaseReference databaseReference;

    public DoorService(){initialize();}

    /**
     *
     * @param doorId
     * @return
     */
    @Override
    public int unlock(String doorId, final String password, final User user) {

        getDoor(doorId, user, (door) -> {
            // Unlock signal to MCU. Use doorId and password for this.
        });

        return 0;
    }

    /**
     *
     * @param door
     * @return
     */
    @Override
    public int add(Door door, User user, final Context context) {
        Task<Void> task = getDatabaseReference().child("users").child(user.getUsername()).child("doors").setValue(door)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Please enter proper credentials!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        if(task.isSuccessful()) return 1;
        return 0;
    }

    @Override
    public Door getDoor(String doorId, User user, FirebaseDoorCallback callback) {
        getDatabaseReference().child("users").child(user.getUsername()).child("doors")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Door door = dataSnapshot.getValue(Door.class);
                        if(door == null) return;
                        callback.onCallback(door);
                        Log.i("RETRIEVED_DOOR", door.toString());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("RETRIEVED_USER", "Door retrieval failed.");
                    }
                });
        return null;
    }

    /**
     *
     * @return
     */
    public DatabaseReference initialize() {
        this.databaseReference = FirebaseDatabase
                .getInstance(Config.FIREBASE_URL)
                .getReference();
        if (this.databaseReference.getDatabase() == null) {
            return null;
        } else {
            Log.i("CONNECTION", "Firebase Connection Successfully Granted for Door Service.");
        }
        return databaseReference;
    }

    private DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}

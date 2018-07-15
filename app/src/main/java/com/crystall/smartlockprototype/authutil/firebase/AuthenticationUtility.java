package com.crystall.smartlockprototype.authutil.firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crystall.smartlockprototype.authutil.IAuthenticationUtililty;
import com.crystall.smartlockprototype.beans.firebase.User;
import com.crystall.smartlockprototype.config.Config;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthenticationUtility implements IAuthenticationUtililty {

    private DatabaseReference databaseReference;
    private final PasswordUtility passwordUtility = new PasswordUtility();

    public AuthenticationUtility() {
        initialize();
    }

    /**
     * Initializes the db connection
     * @return DatabaseReference instance.
     */
    @Override
    public DatabaseReference initialize() {

        this.databaseReference = FirebaseDatabase
                .getInstance(Config.FIREBASE_URL)
                .getReference();

        if(this.databaseReference.getDatabase() == null) {
            return null;
        } else {
            Log.i("CONNECTION", "Firebase Connection Successfully Initialized.");
        }

        return databaseReference;

    }

    /**
     * Writes to the database.
     * @param user
     * @return 0 -> failure 1 -> success.
     */
    @Override
    public int write(User user) {

        int result = 0;

        // Hash the password and store
        user.setPassword(passwordUtility.hash(user.getPassword()));

        Task<Void> users = getDatabaseReference().child("users").child(user.getUsername())
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("SUCCESS", "Successful Data Write");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FAILURE", "Failure Data Write");
                    }
                });

        if(users.isSuccessful()) {
            result = 1;
        }

        return result;
    }

    /**
     * Read from the database.
     * @param username
     * @return User
     */
    @Override
    public User read(String username) {

        final User[] user = {null};

        getDatabaseReference().child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User retrievedUser = dataSnapshot.getValue(User.class);
                if (retrievedUser == null) {
                    return;
                }
                user[0] = retrievedUser;
                Log.i("RETRIEVED_USER", retrievedUser.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FAILED_TO_RETRIEVE_USER", "User Retrieval Failed.");
            }

        });

        return user[0];
    }

    /**
     * Updates user
     * @param user
     * @return 0 -> failure 1 -> success.
     */
    @Override
    public int update(User user) {
        return 0;
    }

    /**
     * Deletes a user.
     * @param name
     * @return 0 -> failure 1 -> success.
     */
    @Override
    public int delete(String name) {
        return 0;
    }

    /**
     * Login utility.
     * @param name
     * @return true if the password is valid.
     */
    @Override
    public boolean login(String name, String password) {
        User user = read(name);
        boolean result = false;

        if(user != null) {
            result = passwordUtility.dehashAndCheck(password, user.getPassword());
        }

        return result;

    }

    /**
     * Gets the database reference to the context of this class.
     * @return DatabaseReference
     */
    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}

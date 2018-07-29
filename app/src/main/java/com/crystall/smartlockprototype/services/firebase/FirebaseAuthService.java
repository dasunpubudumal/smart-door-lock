package com.crystall.smartlockprototype.services.firebase;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.crystall.smartlockprototype.LoggedInActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class FirebaseAuthService implements IFirebaseAuthService{

    private FirebaseAuth mAuth;

    /**
     * FirebaseAuth service initialization
     * @return
     */
    public FirebaseAuth initialilze() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    /**
     * Firebase User Sign Up
     * @param email
     * @param password
     * @param context
     * @return
     */
    @Override
    public int signUp(String email, String password, final Context context) {
        Task<AuthResult> authResultTask = getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> Toast.makeText(context,
                        "Successfull firebase auth user added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context,
                        "Successfull firebase auth user added", Toast.LENGTH_SHORT).show());
        if (authResultTask.isSuccessful()) return 1;
        return 0;
    }

    /**
     * Firebase User Sign In
     * @param email
     * @param password
     * @param context
     * @return
     */
    @Override
    public int signIn(String email, String password, final Context context) {
        Task<AuthResult> authResultTask = getmAuth().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Intent intent = new Intent(context, LoggedInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("USER",
                            (Serializable) FirebaseAuth.getInstance().getCurrentUser());  // Send the user to the next intent.
                    context.startActivity(intent);
                }).addOnFailureListener(e -> Toast.makeText(context,
                        "Please enter proper credentials!",
                        Toast.LENGTH_SHORT).show());
        if (authResultTask.isSuccessful()) return 1;
        return 0;
    }

    private FirebaseAuth getmAuth() {
        return mAuth;
    }
}

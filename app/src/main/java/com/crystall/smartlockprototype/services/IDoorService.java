package com.crystall.smartlockprototype.services;

import android.content.Context;

import com.crystall.smartlockprototype.beans.firebase.Door;
import com.crystall.smartlockprototype.beans.firebase.User;
import com.crystall.smartlockprototype.services.firebase.FirebaseDoorCallback;
import com.google.firebase.database.DatabaseReference;

public interface IDoorService {

    DatabaseReference initialize();

    int unlock(String doorId, String password, User user);

    int add(Door door, User user, Context context);

    Door getDoor(String doorId, User user, FirebaseDoorCallback firebaseDoorCallback);

}

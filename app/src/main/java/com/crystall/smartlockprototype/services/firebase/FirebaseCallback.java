package com.crystall.smartlockprototype.services.firebase;

import com.crystall.smartlockprototype.beans.firebase.Door;
import com.crystall.smartlockprototype.beans.firebase.User;

public interface FirebaseCallback {
    void onCallback(User user);
}

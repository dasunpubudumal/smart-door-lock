package com.crystall.smartlockprototype.services.firebase;

import com.crystall.smartlockprototype.beans.firebase.Door;

public interface FirebaseDoorCallback {
    void onCallback(Door door);
}

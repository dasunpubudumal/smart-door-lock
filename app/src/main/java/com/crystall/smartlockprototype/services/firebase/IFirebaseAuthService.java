package com.crystall.smartlockprototype.services.firebase;

import android.content.Context;

public interface IFirebaseAuthService {

    int signUp(String email, String password, final Context context);

    int signIn(String email, String password, final Context context);
}

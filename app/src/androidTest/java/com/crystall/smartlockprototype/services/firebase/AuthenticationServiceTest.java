package com.crystall.smartlockprototype.services.firebase;

import com.crystall.smartlockprototype.config.Config;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AuthenticationServiceTest {

    private final AuthenticationService authenticationService = new AuthenticationService();

    @Test
    public void testInitialize() {
        assertThat(authenticationService.initialize(), is(FirebaseDatabase
                .getInstance(Config.FIREBASE_URL)
                .getReference()));
    }
}
package org.wow.grollj.auth;

import org.wow.grollj.shared.Realm;
import org.wow.grollj.world.WorldSocketManager;

public class AuthSession {
    private String username;
    private String password;
    private byte[] authSessionKey;
    private WorldSocketManager wsm;
    private Realm realm;


    public AuthSession(String username, String password, WorldSocketManager wsm) {
        this.username = username;
        this.password = password;
        this.wsm = wsm;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAuthSessionKey() {
        return authSessionKey;
    }

    public void setAuthSessionKey(byte[] authSessionKey) {
        this.authSessionKey = authSessionKey;
    }

    public WorldSocketManager getWsm() {
        return wsm;
    }

    public void setWsm(WorldSocketManager wsm) {
        this.wsm = wsm;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }
}

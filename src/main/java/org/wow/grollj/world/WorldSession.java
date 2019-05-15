package org.wow.grollj.world;

import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.auth.AuthSession;

public class WorldSession {
    AuthSession authSession;

    StreamCipher enc;
    StreamCipher dec;
    byte[] digest;
    byte[] salt;
    byte[] seed;
    boolean encryptionActive;

    World world = new World();

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public StreamCipher getEnc() {
        return enc;
    }

    public void setEnc(StreamCipher enc) {
        this.enc = enc;
    }

    public StreamCipher getDec() {
        return dec;
    }

    public void setDec(StreamCipher dec) {
        this.dec = dec;
    }

    public WorldSession(AuthSession authSession) {
        this.authSession = authSession;
    }

    public AuthSession getAuthSession() {
        return authSession;
    }

    public void setAuthSession(AuthSession authSession) {
        this.authSession = authSession;
    }

    public byte[] getDigest() {
        return digest;
    }

    public void setDigest(byte[] digest) {
        this.digest = digest;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getSeed() {
        return seed;
    }

    public void setSeed(byte[] seed) {
        this.seed = seed;
    }

    public boolean isEncryptionActive() {
        return encryptionActive;
    }

    public void setEncryptionActive(boolean encryptionActive) {
        this.encryptionActive = encryptionActive;
    }
}

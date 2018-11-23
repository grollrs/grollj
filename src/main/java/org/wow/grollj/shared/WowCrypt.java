package org.wow.grollj.shared;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.wow.grollj.auth.AuthSession;
import org.wow.grollj.auth.messages.C_AuthLogonProof;
import org.wow.grollj.auth.messages.S_AuthLogonChallenge;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.auth.S_AuthChallenge;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public class WowCrypt {

    AuthSession session;

    public WowCrypt(AuthSession session) {
        this.session = session;
    }

    public C_AuthLogonProof handleAuthLogonChallenge(S_AuthLogonChallenge salc){

        BigInteger N = new BigInteger(1,salc.getN());
        BigInteger g = new BigInteger(1,salc.getG());
        BigInteger b = new BigInteger(1,salc.getB());

        byte[] salt = salc.getS();
        byte[] identity = session.getUsername().getBytes();
        byte[] password = session.getPassword().getBytes();


        SecureRandom secureRandom = new SecureRandom();
        Digest digest = new SHA1Digest();

        BigInteger v= generateVerifier(salt,identity,password, digest,N,g);

        //calculate creadentials
        BigInteger x = calculateX(N, salt, identity, password, digest);

        //this is hardcoded for now for reproducability
        BigInteger a = new BigInteger("00000000000000000000000000861565895658c4b0118940b7245c2f264ccc72",16);
        BigInteger A = g.modPow(a, N);


        //compute o
        byte[] ob=new byte[digest.getDigestSize()];
        byte[] ar = A.toByteArray();
        ArrayUtils.reverse(ar);
        byte[] br = trimBigIntegerBytes(b.toByteArray());
        ArrayUtils.reverse(br);
        digest.update(ar,0,ar.length);
        digest.update(br,0,br.length);
        digest.doFinal(ob, 0);
        ArrayUtils.reverse(ob);


        BigInteger o = new BigInteger(1, ob);
        BigInteger kv = BigInteger.valueOf(3).multiply(v);
        BigInteger aox = a.add(o.multiply(x));
        BigInteger diff = b.subtract(kv);

        BigInteger key = diff.modPow(aox,N);

        // shuffle key to generate K
        byte[] ds0 = new byte[digest.getDigestSize()];
        byte[] ds1 = new byte[digest.getDigestSize()];

        byte[] sBytes = trimBigIntegerBytes(key.toByteArray());
        byte[] sub0 = new byte[sBytes.length/2];
        byte[] sub1 = new byte[sBytes.length/2];
        for(int i = 0; i<sBytes.length;i+=2){
            sub0[i/2]=sBytes[i];
        }
        for(int i = 1; i<sBytes.length;i+=2){
            sub1[i/2]=sBytes[i];
        }
        ArrayUtils.reverse(sub0);
        ArrayUtils.reverse(sub1);

        digest.update(sub0,0,sub0.length);
        digest.doFinal(ds0,0);
        digest.update(sub1,0,sub1.length);
        digest.doFinal(ds1,0);

        ArrayUtils.reverse(ds0);
        ArrayUtils.reverse(ds1);

        byte[] k = new byte[2*digest.getDigestSize()];

        //interleave the digests
        for(int i = 0 ; i<k.length;i++){
            if(i%2==0){
                k[i]=ds0[i/2];
            }
            else{
                k[i]=ds1[i/2];
            }
        }

        byte[] nBytes=trimBigIntegerBytes(N.toByteArray());


        ArrayUtils.reverse(nBytes);
        digest.update(nBytes,0,nBytes.length);
        byte[] prime = new byte[digest.getDigestSize()];
        digest.doFinal(prime,0);
        ArrayUtils.reverse(prime);


        byte[] gBytes=g.toByteArray();
        ArrayUtils.reverse(gBytes);
        digest.update(gBytes,0,gBytes.length);
        byte[] gen = new byte[digest.getDigestSize()];
        digest.doFinal(gen,0);
        ArrayUtils.reverse(gen);


        byte[] ngh = new byte[digest.getDigestSize()];
        for(int i = 0 ; i<ngh.length;i++){
            ngh[i]= (byte) (prime[i] ^ gen[i]);
        }


        byte[] ih = new byte[digest.getDigestSize()];
        digest.update(identity,0,identity.length);
        digest.doFinal(ih,0);

        ArrayUtils.reverse(ngh);
        ArrayUtils.reverse(k);

        session.setAuthSessionKey(k);

        /*
        System.out.println("A: " + A.toString(16));
        System.out.println("v: " + v.toString(16));
        System.out.println("S: " + key.toString(16));
        System.out.println("o: " + Hex.encodeHexString(ob));
        System.out.println("---------");
        System.out.println("ngh: " + Hex.encodeHexString(ngh));
        System.out.println("ih: " + Hex.encodeHexString(ih));
        System.out.println("salt: " + Hex.encodeHexString(salt));
        System.out.println("ar: " + Hex.encodeHexString(ar));
        System.out.println("br: " + Hex.encodeHexString(br));
        System.out.println("k: " + Hex.encodeHexString(k));
        System.out.println("---------");
        */


        //calculate m1 for message
        byte[] m1 = new byte[digest.getDigestSize()];
        digest.update(ngh,0,ngh.length);
        digest.update(ih,0,ih.length);
        digest.update(salt,0,salt.length);
        digest.update(ar,0,ar.length);
        digest.update(br,0,br.length);
        digest.update(k,0,k.length);
        digest.doFinal(m1,0);
        System.out.println("M1: " + Hex.encodeHexString(m1));

        try {
            C_AuthLogonProof proof = new C_AuthLogonProof(ar,m1,Hex.decodeHex("288900a60dae387aeb4335ca9b48a6c0d3122442"));
            return proof;
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        return null;

    }

    // since BigInts do weird stuff, they sometimes append an extra 0x00 byte infront of the string
    private byte[] trimBigIntegerBytes(byte[] toByteArray) {
        if(toByteArray.length>32){
            toByteArray = Arrays.copyOfRange(toByteArray,1,33);
        }
        return toByteArray;
    }


    public BigInteger generateVerifier(byte[] salt, byte[] identity, byte[] password,Digest digest, BigInteger N, BigInteger g)
    {
        BigInteger x = calculateX( N, salt, identity, password,digest);
        BigInteger v = g.modPow(x, N);
        return v;
    }


    public BigInteger calculateX(BigInteger N, byte[] salt, byte[] identity, byte[] password, Digest digest)
    {
        byte[] output = new byte[digest.getDigestSize()];

        digest.update(identity, 0, identity.length);
        digest.update((byte)':');
        digest.update(password, 0, password.length);
        digest.doFinal(output, 0);

        digest.update(salt, 0, salt.length);
        digest.update(output, 0, output.length);
        digest.doFinal(output, 0);

        //this line has cost me 2h
        ArrayUtils.reverse(output);
        BigInteger res = new BigInteger(1, output);
        return res.mod(N);
    }


    public void handleAuthChallenge(S_AuthChallenge challenge, WorldSession session){

        byte [] s1 = challenge.getSeed1();
        byte [] s2 = challenge.getSeed2();
        byte[] salt = challenge.getSalt();
        byte[] key = this.session.getAuthSessionKey();

        // the sessionkey is already reversed due to the network

        /*
        byte [] key = new byte[0];
        byte [] s1 = new byte[0];
        byte [] s2 = new byte[0];
        */

        try {
            //key = Hex.decodeHex("2C675A16540A7E6E554EADB22C5C41652B30BF10CEB6FB9ECBF37D0F9BA68FCB18382E3455461546");
            //ArrayUtils.reverse(key);
            s1 = Hex.decodeHex("C2B3723CC6AED9B5343C53EE2F4367CE");
            s2 = Hex.decodeHex("CC98AE04E897EACA12DDC09342915357");
        } catch (DecoderException e) {
            e.printStackTrace();
        }


        HMac enc_mac = new HMac(new SHA1Digest());
        enc_mac.init(new KeyParameter(s1));
        //enc_mac.update(s1,0,s1.length);
        enc_mac.update(key,0,key.length);

        HMac dec_mac = new HMac(new SHA1Digest());
        dec_mac.init(new KeyParameter(s2));
        //dec_mac.update(s2,0,s2.length);
        dec_mac.update(key,0,key.length);

        byte[] enc_session_key = new byte[enc_mac.getMacSize()];
        enc_mac.doFinal(enc_session_key,0);
        byte[] dec_session_key = new byte[dec_mac.getMacSize()];
        dec_mac.doFinal(dec_session_key,0);

        StreamCipher enc = new RC4Engine();
        enc.init(true, new KeyParameter(enc_session_key));

        StreamCipher dec = new RC4Engine();
        dec.init(false, new KeyParameter(dec_session_key));

        byte [] padding = new byte[1024];
        byte [] out = new byte[1024];
        enc.processBytes(padding,0,padding.length,out,0);
        dec.processBytes(padding,0,padding.length,out,0);

        System.out.println("Key:" + Hex.encodeHexString(key));
        System.out.println("s1:" + Hex.encodeHexString(s1));
        System.out.println("s2:" + Hex.encodeHexString(s2));




        /*
        const hash = new SHA1();
        hash.feed(this.session.auth.account);
        hash.feed([0, 0, 0, 0]);
        hash.feed(seed.toArray());
        hash.feed(salt);
        hash.feed(this.session.auth.key);
        */




        byte[] seedTarget = new byte[4];
        new Random().nextBytes(seedTarget);
        session.setSeed(seedTarget);

        Digest sha = new SHA1Digest();
        sha.update(session.getAuthSession().getPassword().getBytes(),0,session.getAuthSession().getPassword().getBytes().length);
        sha.update(new byte[]{0x00,0x00,0x00,0x00},0,4);
        byte[] localSeed = ArrayUtils.clone(session.getSeed());
        ArrayUtils.reverse(localSeed);
        sha.update(localSeed,0,session.getSeed().length);
        sha.update(session.getSalt(),0,session.getSalt().length);
        sha.update(key,0,key.length);
        byte[] digest = new byte[sha.getDigestSize()];
        sha.doFinal(digest,0);

        ArrayUtils.reverse(digest);

        session.setEnc(enc);
        session.setDec(dec);
        session.setDigest(digest);

        System.out.println("Digest: " + Hex.encodeHexString(digest));
    }

}

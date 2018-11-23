package org.wow.grollj.world.packet.auth;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.world.packet.CPacket;
import org.wow.grollj.world.packet.WorldOpcodes;

import java.nio.ByteBuffer;

public class C_AuthChallenge implements CPacket {

    String account;
    byte[] seed;
    byte[] digest;

    int length=-1;

    public C_AuthChallenge(String account, byte[] seed, byte[] digest) {
        this.account = account;
        this.seed = seed;
        this.digest = digest;
        length = account.length()+1 +60;

    }

    @Override
    public byte[] getBytes() {

        /*
        00000000  01 1c 						                        len
                        ed 01 00 00 					                oc
                                    34 30  00 00 		   	            build
                                                 00 00 00 00 		    0
                                                             50 4c
        00000010  41 59 45 52 00 				     	                cstring accountname
                                00 00 00   00 				            0
                                              29 84 d0 83 		        salt
                                                          00 00 00
        00000020  00 00 00 00 00 01 00 00  00 00 00 00 00 00 00 00
        00000030  00 							                        0
                     c5 f7 17 92 66 78 5e  8e 8d 85 97 a7 00 a6 5b
        00000040  cb 8a 19 50 ad 		                    			digest
                     9e 02 00  00 				                        addon-data
         */

        ByteBuffer bb = ByteBuffer.allocate(length);
        bb.put(new byte[]{0x34,0x30,0x00,0x00});      // build
        bb.putInt(0);                       // loginserverid
        bb.put(account.getBytes());         // account
        bb.put(new byte[]{0x00});           // string terminator
        bb.putInt(0);                       // login server type
        byte[] localSeed = ArrayUtils.clone(seed);
        ArrayUtils.reverse(localSeed);
        bb.put(localSeed);                  // challenge
        bb.putInt(0);                       // regioid
        bb.putInt(0);                       // battlegroupid
        bb.put(new byte[]{0x01,0x00,0x00,0x00});    // realmid
        bb.putInt(0);                       // response
        bb.putInt(0);                       // response2
        ArrayUtils.reverse(digest);
        bb.put(digest);                     // digest
        bb.putInt(0);                       // addon info?

        return bb.array();
    }

    @Override
    public byte[] getHeader(StreamCipher cipher) {
        //total = payload + oc-size
        short l = (short) (length+4);
        byte low = (byte) (l/256);
        byte high = (byte) (l%256);
        byte[] lengthBytes = new byte[]{low,high};

        byte[] ocBytes = ByteBuffer.allocate(4).putInt(WorldOpcodes.CMSG_AUTH_SESSION.getOpcode()).array();
        ArrayUtils.reverse(ocBytes);


        ByteBuffer bb = ByteBuffer.allocate(6);
        bb.put(lengthBytes);
        bb.put(ocBytes);

        System.out.println(Hex.encodeHexString(bb.array()));

        // do not enrypt this header

        return bb.array();
    }
}

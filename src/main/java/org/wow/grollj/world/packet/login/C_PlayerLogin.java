package org.wow.grollj.world.packet.login;

import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.world.packet.CPacket;

public class C_PlayerLogin implements CPacket {

    // CMSG_PLAYER_LOGIN (0x03D),

    @Override
    public byte[] getBytes() {
        // return the char GUID
        return new byte[]{0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    }

    @Override
    public byte[] getHeader(StreamCipher cipher) {
        // len 8+4 = 0x00 0c
        // oc   [4]  0x00 00 00 3d (rev)
        // guid [8] [body]
        return new byte[]{0x00,0x0C,0x3D,0x00,0x00,0x00};
    }

}

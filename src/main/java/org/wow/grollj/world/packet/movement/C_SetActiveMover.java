package org.wow.grollj.world.packet.movement;

import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.world.packet.CPacket;

public class C_SetActiveMover implements CPacket {

    // CMSG_SET_ACTIVE_MOVER (0x26A)

    @Override
    public byte[] getBytes() {
        return new byte[]{0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    }

    @Override
    public byte[] getHeader(StreamCipher cipher) {
        return new byte[]{0x00,0x0C,0x6A,0x02,0x00,0x00};
    }

}

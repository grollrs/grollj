package org.wow.grollj.world.packet;

import org.bouncycastle.crypto.StreamCipher;

public class C_PlayerLogin implements CPacket {
    @Override
    public byte[] getBytes() {
        return new byte[]{0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    }

    @Override
    public byte[] getHeader(StreamCipher cipher) {
        return new byte[]{0x00,0x0C,0x3D,0x00,0x00,0x00};
    }

}

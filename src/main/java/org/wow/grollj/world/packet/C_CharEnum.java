package org.wow.grollj.world.packet;

import org.bouncycastle.crypto.StreamCipher;

public class C_CharEnum implements CPacket {
    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public byte[] getHeader(StreamCipher cipher) {
        return new byte[]{0x00,0x04,0x37,0x00,0x00,0x00};
    }
}

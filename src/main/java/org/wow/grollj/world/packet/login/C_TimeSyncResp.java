package org.wow.grollj.world.packet.login;

import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.world.packet.CPacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class C_TimeSyncResp implements CPacket {

    private int ctr;
    private int time;

    public C_TimeSyncResp(int ctr, int time) {
        this.ctr = ctr;
        this.time = time;
    }

    @Override
    public byte[] getBytes() {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(ctr);
        buf.putInt(time);
        return buf.array();
    }

    @Override
    public byte[] getHeader(StreamCipher cipher) {
        // len 8+4 = 0x00 0c
        // oc   [4]  0x00 00 03 91 (rev)
        // ctr  [4]
        // time [4]
        return new byte[]{
                0x00,0x0c,
                (byte) 0x91,
                (byte) 0x03,
                (byte) 0x00,
                (byte) 0x00
        };
    }
}

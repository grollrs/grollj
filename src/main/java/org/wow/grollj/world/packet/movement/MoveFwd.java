package org.wow.grollj.world.packet.movement;

import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.world.packet.CPacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MoveFwd implements CPacket {

    private Position pos;
    private int time;

    public MoveFwd(Position pos, int time) {
        this.pos = pos;
        this.time = time;
    }

    @Override
    public byte[] getBytes() {

        System.out.println("Moving: pos " + pos);

        ByteBuffer buf = ByteBuffer.allocate(32);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.put(new byte[]{0x02,0x0});  // guid
        buf.putInt(0x00000001);         // flags
        buf.put(new byte[]{0x00,0x0});  // flags2
        buf.putInt(time);               // worldtime
        pos.writeToByteBuffer(buf);     // XYZO stream
        buf.putInt(0);                  // falltime

        System.out.println("Movement len: "+buf.array().length);
        return buf.array();
    }

    @Override
    public byte[] getHeader(StreamCipher cipher) {
        /*
            len =  4 oc
                   8 guid
                   4 falgs
                   2 flags2
                   4 time
                   4*4 xyzo stream
                   4 falltime
                = 42
         */
        return new byte[]{
                0x00,
                0x24,

                (byte) 0xB5,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00,
        };
    }
}

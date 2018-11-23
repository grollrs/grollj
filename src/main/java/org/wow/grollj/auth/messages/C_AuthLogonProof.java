package org.wow.grollj.auth.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.unix.Buffer;

import java.nio.ByteBuffer;

public class C_AuthLogonProof implements CMessage{
    private byte cmd;
    private byte [] a;
    private byte [] m1;
    private byte [] crc;

    public C_AuthLogonProof(byte[] a, byte[] m1, byte[] crc) {
        this.a = a;
        this.m1 = m1;
        this.crc = crc;
        cmd=0x01;
    }

    @Override
    public byte[] getBytes() {
        int innerLen = a.length + m1.length + crc.length;
        int totalLen = innerLen +3;
        ByteBuffer buf = ByteBuffer.allocate(totalLen);
        buf.put(cmd);
        buf.put(a);
        buf.put(m1);
        buf.put(crc);
        buf.put(new byte[]{0x00,0x00});
        return buf.array();
    }
}

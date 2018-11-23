package org.wow.grollj.world.packet;

import org.bouncycastle.crypto.StreamCipher;

public interface CPacket {
    byte[] getBytes();
    byte[] getHeader(StreamCipher cipher);
}

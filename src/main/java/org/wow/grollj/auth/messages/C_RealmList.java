package org.wow.grollj.auth.messages;

public class C_RealmList implements CMessage {
    @Override
    public byte[] getBytes() {
        // this is just the opcode and 4 trailing zero bytes
        return new byte[]{0x10,0x0,0x0,0x0,0x0};
    }
}

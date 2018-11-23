package org.wow.grollj.auth.messages;

public enum AuthOpcodes {
    C_AUTH_LOGON_CHALLENGE(0x00),
    S_AUTH_LOGON_CHALLENGE(0x00),
    C_AUTH_LOGON_PROOF(0x01),
    S_AUTH_LOGON_PROOF(0x01),
    C_REALM_LIST(0x10),
    S_REALM_LIST(0x10);

    private final byte opcode;

    AuthOpcodes(int i) {
        opcode= (byte) i;
    }

    public byte getOpcode(){
        return this.opcode;
    }
}

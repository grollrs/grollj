package org.wow.grollj.auth.messages;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;

public class AuthMessageFactory {

    static HashMap<Byte,SMessageProducer> producers;


    /**
     * We add all possible producers in a map, so we can handle them fast, when the opcodes are known
     */
    static{
        producers=new HashMap<>();
        producers.put(S_AuthLogonChallenge.getOpcode(),S_AuthLogonChallenge.getProducer());
        producers.put(S_AuthLogonProof.getOpcode(),S_AuthLogonProof.getProducer());
        producers.put(S_RealmList.getOpcode(),S_RealmList.getProducer());
    }

    public static SMessage produce(byte opcode, ByteBuf in){
        SMessageProducer smp = producers.get(opcode);
        if (smp==null){
            return null;
        }
        return smp.produce(in);
    }
}

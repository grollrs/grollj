package org.wow.grollj.auth.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.wow.grollj.auth.AuthSession;

public class S_AuthLogonProof implements SMessage {


    private final static int MIN_LEN =32;

    public static SMessageProducer getProducer() {
        return new S_AuthLogonProofProducer();
    }

    public static byte getOpcode() {
        return AuthOpcodes.S_AUTH_LOGON_PROOF.getOpcode();
    }

    @Override
    public void handle(AuthSession session, ChannelHandlerContext ctx) {
        System.out.println("Handle S_AuthLogonProof");
        ctx.writeAndFlush(new C_RealmList());
    }

    public static class S_AuthLogonProofProducer implements SMessageProducer<S_AuthLogonProof>{

        @Override
        public S_AuthLogonProof produce(ByteBuf in) {
            if(in.readableBytes()< MIN_LEN){
                return null;
            }

            byte command = in.readByte();
            short error = in.readByte();

            byte[] b = new byte[20];
            in.readBytes(b);

            byte[] b2 = new byte[10];
            in.readBytes(b2);

            return new S_AuthLogonProof();
        }
    }
}

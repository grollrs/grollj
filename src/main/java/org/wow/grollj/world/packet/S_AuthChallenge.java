package org.wow.grollj.world.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.ArrayUtils;
import org.wow.grollj.shared.WowCrypt;
import org.wow.grollj.world.WorldSession;

public class S_AuthChallenge implements SPacket {

    byte[] salt;
    byte[] seed1;
    byte[] seed2;

    public S_AuthChallenge(byte[] salt, byte[] seed1, byte[] seed2) {
        this.salt = salt;
        this.seed1 = seed1;
        this.seed2 = seed2;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getSeed1() {
        return seed1;
    }

    public byte[] getSeed2() {
        return seed2;
    }

    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {
        System.out.println("[W] Handle S_AuthChallenge");
        WowCrypt crypt = new WowCrypt(session.getAuthSession());
        session.setSalt(salt);
        crypt.handleAuthChallenge(this, session);

        ctx.writeAndFlush(new C_AuthChallenge(session.getAuthSession().getUsername(),session.getSeed(),session.getDigest()));

    }

    public static SPacketProducer<S_AuthChallenge> getProducer() {
        return new S_AuthChallengeProducer();
    }

    static class S_AuthChallengeProducer implements SPacketProducer<S_AuthChallenge>{

        @Override
        public S_AuthChallenge produce(ByteBuf in) {

            //u32 ?
            //u32 authSeed
            //16byte seed1
            //16byte seed2

            in.readInt();
            byte[] salt = new byte[4];
            byte[] seed1 = new byte[16];
            byte[] seed2 = new byte[16];
            in.readBytes(salt);
            in.readBytes(seed1);
            in.readBytes(seed2);

            ArrayUtils.reverse(seed1);
            ArrayUtils.reverse(seed2);

            return new S_AuthChallenge(salt,seed1,seed2);
        }
    }
}

package org.wow.grollj.world.packet.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.login.C_CharEnum;
import org.wow.grollj.world.packet.SPacket;
import org.wow.grollj.world.packet.SPacketProducer;

public class S_AuthResponse implements SPacket {


    private byte code;

    private S_AuthResponse(byte code) {
        this.code = code;
    }

    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {
        System.out.println("Auth Response: "+(code==12? "SUCCESS":"ERR"));
        session.setEncryptionActive(true);
        ctx.writeAndFlush(new C_CharEnum());
    }

    public static SPacketProducer<S_AuthResponse> getProducer() {
        return new S_AuthResponseProducer();
    }

    static class S_AuthResponseProducer implements SPacketProducer<S_AuthResponse>{

        @Override
        public S_AuthResponse produce(ByteBuf in) {

            byte code = in.readByte();

            return new S_AuthResponse(code);
        }
    }
}

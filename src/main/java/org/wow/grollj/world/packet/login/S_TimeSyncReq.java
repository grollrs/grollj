package org.wow.grollj.world.packet.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.SPacket;
import org.wow.grollj.world.packet.SPacketProducer;

public class S_TimeSyncReq implements SPacket {

    int ctr;

    public S_TimeSyncReq(int ctr) {
        this.ctr = ctr;
    }

    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {
        System.out.println("Rcv TimeSyncReq: ctr:" + ctr);
        ctx.writeAndFlush(new C_TimeSyncResp(ctr,session.getWorld().getServerTime().getCurrentServerTime()));
    }

    public static SPacketProducer<S_TimeSyncReq> getProducer() {
        return new S_TimeSyncReq.S_TimeSyncReqProd();
    }

    static class S_TimeSyncReqProd implements SPacketProducer<S_TimeSyncReq>{

        @Override
        public S_TimeSyncReq produce(ByteBuf in) {
            int ctr = in.readIntLE();
            return new S_TimeSyncReq(ctr);
        }
    }

}

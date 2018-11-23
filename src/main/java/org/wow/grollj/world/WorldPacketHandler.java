package org.wow.grollj.world;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.wow.grollj.world.packet.SPacket;

public class WorldPacketHandler extends SimpleChannelInboundHandler<SPacket> {

    WorldSession worldSession;

    public WorldPacketHandler(WorldSession worldSession) {
        this.worldSession = worldSession;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SPacket msg) throws Exception {
        msg.handle(worldSession,ctx);
    }
}

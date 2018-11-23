package org.wow.grollj.world.packet;

import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.world.WorldSession;

public interface SPacket {
    void handle(WorldSession session, ChannelHandlerContext ctx);
}

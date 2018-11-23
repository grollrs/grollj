package org.wow.grollj.world.packet;

import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.world.WorldSession;

public class S_MalformedPacket implements SPacket {
    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {
        System.out.println("Handling malformed packet");
    }
}

package org.wow.grollj.world.packet.misc;

import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.SPacket;

public class S_MalformedPacket implements SPacket {
    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {
        System.out.println("Handling malformed packet");
    }
}

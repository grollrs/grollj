package org.wow.grollj.auth.messages;

import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.auth.AuthSession;

public class UnknownMessage implements SMessage {
    @Override
    public void handle(AuthSession session, ChannelHandlerContext ctx) {
        System.out.println("Handle UnknownMessage - ignore -");
    }
}

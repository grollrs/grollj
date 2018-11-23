package org.wow.grollj.auth.messages;

import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.auth.AuthSession;

public interface SMessage {

    void handle(AuthSession session, ChannelHandlerContext ctx);

}

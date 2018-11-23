package org.wow.grollj.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wow.grollj.auth.messages.AuthMessageFactory;
import org.wow.grollj.auth.messages.CMessage;
import org.wow.grollj.auth.messages.SMessage;


public class AuthEncoder extends MessageToByteEncoder<CMessage> {
    Logger logger = LogManager.getLogger(AuthEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, CMessage msg, ByteBuf out) throws Exception {

        logger.debug("[N] Encoding message");
        out.writeBytes(msg.getBytes());
    }
}

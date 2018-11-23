package org.wow.grollj.auth;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wow.grollj.auth.messages.AuthMessageFactory;
import org.wow.grollj.auth.messages.SMessage;

import java.util.List;

public class AuthDecoder extends ByteToMessageDecoder {
    Logger logger = LogManager.getLogger(AuthDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes()<1) {
            return;
        }

        in.markReaderIndex();

        //read command byte
        byte command = in.readByte();
        logger.debug("[N] Decoding " + command);
        // rest reader so we will read the opcode again in the factory
        in.resetReaderIndex();


        SMessage msg =AuthMessageFactory.produce(command, in);
        if (msg==null){
            logger.debug("[N] Not enough bytes for OC:" + command);
            return;
        }
        else{
            logger.debug("[N] Yielding " + msg);
            out.add(msg);
        }
    }
}

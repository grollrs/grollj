package org.wow.grollj.world.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.CPacket;

public class WorldEncoder extends MessageToByteEncoder<CPacket> {
    Logger logger = LogManager.getLogger(WorldEncoder.class);

    WorldSession session;
    StreamCipher enc;

    public WorldEncoder(WorldSession session) {
        this.session = session;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CPacket msg, ByteBuf out) throws Exception {
        logger.debug("[W] Encoding message");
        byte[] header = msg.getHeader(session.getEnc());
        if(enc==null){
            if(session.isEncryptionActive()){
                enc=session.getEnc();
            }
        }
        if(enc!=null){
            enc.processBytes(header,0,header.length,header,0);
        }
        out.writeBytes(header);
        out.writeBytes(msg.getBytes());
        ctx.flush();
    }
}

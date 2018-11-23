package org.wow.grollj.world.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.StreamCipher;
import org.wow.grollj.auth.messages.SMessage;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.PacketFactory;
import org.wow.grollj.world.packet.SPacket;

import java.util.List;

public class WorldDecoder extends ByteToMessageDecoder {
    Logger logger = LogManager.getLogger(WorldDecoder.class);

    private final static int HEADER_LENGTH = 4;

    private boolean needsDecryption=false;

    private WorldSession session;
    private StreamCipher dec;

    private boolean isBufferHot = false;
    private byte[] headerBuffer = new byte[4];

    public WorldDecoder(WorldSession session) {
        this.session = session;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int minimumBytes = HEADER_LENGTH;
        //TODO are there empty packets with size zero?
        if(isBufferHot){
            minimumBytes-=HEADER_LENGTH;
        }
        if(in.readableBytes()<minimumBytes){
            return;
        }

        byte[] header;
        if(isBufferHot){
            // just pop the already decrypted header from the buffer
            header = ArrayUtils.clone(headerBuffer);
        }
        else{
            header = new byte[HEADER_LENGTH];
            in.readBytes(header);

            // first check thread-local variable
            if (!needsDecryption){
                // ... then check shared mem
                if(session.getDec()!=null){
                    needsDecryption=true;
                    dec = session.getDec();
                }
            }
            // check local variable again
            if (needsDecryption){
                dec.processBytes(header,0,header.length,header,0);
            }
            // read header data
        /*  TODO: Large packets
            large packets:
             - if second/first byte is larger than 0x7f, then there is a third length byte that should be
             - read
             - decrypted
             - added to the packet length
             - the oc pos is moved
             - we need two/one large header byte buffer
         */
        }


        // reassemble the original length and oc as integer, since java bytes are signed
        int  len = ((header[0] & 0xff) << 8) | (header[1] & 0xff);
        int oc = ((header[3] & 0xff) << 8) | (header[2] & 0xff);

        // we already have read the OC in this case, therefore substract its size
        len = len -2;

        // we only read data when the whole packet is available and we do not re-read the header, so we
        //  do not need in.markReaderIndex(); here

        System.out.println("Header: [OC]: " +oc +" [len]: "+len );

        // we wait until the whole packet has arrived, so the packet handlers do not have to implement buffer logic
        if(in.readableBytes()<len){
            logger.debug("[N] Not enough bytes for OC:" + oc + ", readable: "+ in.readableBytes() + " len:" + len);
            isBufferHot=true;
            headerBuffer = ArrayUtils.clone(header);
            // since nothing is read, we don't need in.resetReaderIndex();
            return;
        }

        /*
        byte [] payload = new byte[len];
        in.readBytes(payload);
        ByteBuf bb = ByteBufUtil.threadLocalDirectBuffer();
        bb.writeBytes(payload);
        */

        SPacket msg = PacketFactory.produce(oc,in,len);
        // msg cannot be null

        // the packet could be fully read so we do not need the buffered header anymore
        isBufferHot=false;


        out.add(msg);
    }
}

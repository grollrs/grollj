package org.wow.grollj.auth.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.wow.grollj.auth.AuthSession;
import org.wow.grollj.shared.WowCrypt;

public class S_AuthLogonChallenge implements SMessage {


    private final static int MIN_LEN =1+2+32+1;

    private byte command;
    private short error;


    private byte[] b;
    private byte[] g;
    private byte[] n;
    private byte[] s;


    public byte[] getB() {
        return b;
    }

    public byte[] getG() {
        return g;
    }

    public byte[] getN() {
        return n;
    }

    public byte[] getS() {
        return s;
    }


    public static SMessageProducer getProducer() {
        return new S_AuthLogonChallengeProducer();
    }

    public static byte getOpcode() {
        return AuthOpcodes.S_AUTH_LOGON_CHALLENGE.getOpcode();
    }

    @Override
    public void handle(AuthSession session, ChannelHandlerContext ctx) {
        System.out.println("handle S_AuthLogonChallenge");
        WowCrypt cus = new WowCrypt(session);
        C_AuthLogonProof proof = cus.handleAuthLogonChallenge(this);
        ctx.writeAndFlush(proof);
    }

    public static class S_AuthLogonChallengeProducer implements SMessageProducer<S_AuthLogonChallenge>{

        @Override
        public S_AuthLogonChallenge produce(ByteBuf in) {
            if(in.readableBytes()< MIN_LEN){
                return null;
            }

            byte command = in.readByte();
            short error = in.readShortLE();

            byte[] b = new byte[32];

            in.readBytes(b);

            System.out.println("Cmd: "+command + " , Err: " + error + " ,B: " + Hex.encodeHexString(b));

            byte gl =  in.readByte();
            if(in.readableBytes()<gl){
                in.resetReaderIndex();
                return null;
            }
            byte[] g = new byte[gl];
            in.readBytes(g);

            byte nl =  in.readByte();
            if(in.readableBytes()<nl){
                in.resetReaderIndex();
                return null;
            }
            byte[] n = new byte[nl];
            in.readBytes(n);

            if(in.readableBytes()<32+16+1){
                in.resetReaderIndex();
                return null;
            }
            byte[] s = new byte[32];
            in.readBytes(s);

            in.readBytes(16+1); //read trailing garbage


            S_AuthLogonChallenge pkt = new S_AuthLogonChallenge();

            pkt.command=command;
            pkt.error=command;

            ArrayUtils.reverse(b);
            ArrayUtils.reverse(g);
            //ArrayUtils.reverse(s);
            ArrayUtils.reverse(n);


            System.out.println(Hex.encodeHexString(g));
            System.out.println(Hex.encodeHexString(n));
            System.out.println(Hex.encodeHexString(b));
            System.out.println(Hex.encodeHexString(s));

            pkt.b=b;
            pkt.g=g;
            pkt.s=s;
            pkt.n=n;

            return pkt;


            //pre len
            //readlen
            //cmplen


        }
    }
}

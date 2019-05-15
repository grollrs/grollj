package org.wow.grollj.world.packet;

import io.netty.buffer.ByteBuf;
import org.wow.grollj.world.packet.auth.S_AuthChallenge;
import org.wow.grollj.world.packet.auth.S_AuthResponse;
import org.wow.grollj.world.packet.login.S_Account_Data_Times;
import org.wow.grollj.world.packet.login.S_CharEnum;
import org.wow.grollj.world.packet.login.S_LoginVerifyWorld;
import org.wow.grollj.world.packet.login.S_TimeSyncReq;
import org.wow.grollj.world.packet.misc.S_UnknownPacket;

import java.util.HashMap;

public class PacketFactory {
    static HashMap<Integer, SPacketProducer<? extends  SPacket>> producers = new HashMap<>();
    static {
        producers.put(WorldOpcodes.SMSG_AUTH_CHALLENGE.getOpcode(), S_AuthChallenge.getProducer());
        producers.put(WorldOpcodes.SMSG_AUTH_RESPONSE.getOpcode(), S_AuthResponse.getProducer());
        producers.put(WorldOpcodes.SMSG_CHAR_ENUM.getOpcode(), S_CharEnum.getProducer());
        producers.put(WorldOpcodes.SMSG_LOGIN_VERIFY_WORLD.getOpcode(), S_LoginVerifyWorld.getProducer());
        producers.put(WorldOpcodes.SMSG_TIME_SYNC_REQ.getOpcode(), S_TimeSyncReq.getProducer());
        producers.put(WorldOpcodes.SMSG_ACCOUNT_DATA_TIMES.getOpcode(), S_Account_Data_Times.getProducer());
    }

    public static SPacket produce(int opcode, ByteBuf in, int len){
        int startReadable = in.readableBytes();
        SPacketProducer spp = producers.get(opcode);
        if (spp==null){
            in.readBytes(len);
            return new S_UnknownPacket();
        }
        SPacket packet = spp.produce(in);
        // check if we catched all bytes
        if(in.readableBytes()>startReadable-len){
            System.out.println("SPP did produce too short packet, throwing away "+(in.readableBytes()-(startReadable-len)+"bytes"));
            // we need to throw away the unread bytes to align with packet boundaries
            in.readBytes(in.readableBytes()-(startReadable-len));
        }
        if (packet==null){
            return new S_UnknownPacket();
        }
        return packet;
    }
}

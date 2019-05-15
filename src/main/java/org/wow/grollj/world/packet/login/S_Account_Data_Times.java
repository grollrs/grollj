package org.wow.grollj.world.packet.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.world.ServerTime;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.SPacket;
import org.wow.grollj.world.packet.SPacketProducer;

public class S_Account_Data_Times implements SPacket {

    int serverTime;

    public S_Account_Data_Times(int serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {
        int localTime = (int) (System.currentTimeMillis()/1000);
        ServerTime serverTime = new ServerTime(this.serverTime, localTime,localTime-this.serverTime);
        System.out.println("Using at "+localTime+" received ServerTime: "+serverTime.toString());
        session.getWorld().setServerTime(serverTime);
    }

    public static SPacketProducer<S_Account_Data_Times> getProducer() {
        return new S_Account_Data_Times.S_Account_Data_TimesProd();
    }

    static class S_Account_Data_TimesProd implements SPacketProducer<S_Account_Data_Times>{

        @Override
        public S_Account_Data_Times produce(ByteBuf in) {

            /*
            WorldPacket data(SMSG_ACCOUNT_DATA_TIMES, 4 + 1 + 4 + NUM_ACCOUNT_DATA_TYPES * 4);
            data << uint32(GameTime::GetGameTime());                             // Server time
            data << uint8(1);
            data << uint32(mask);                                   // type mask
            for (uint32 i = 0; i < NUM_ACCOUNT_DATA_TYPES; ++i)
                if (mask & (1 << i))
                    data << uint32(GetAccountData(AccountDataType(i))->Time);// also unix time
            SendPacket(&data);
             */
            int serverTime = in.readIntLE();
            // don't care about the rest
            return new S_Account_Data_Times(serverTime);
        }
    }
}

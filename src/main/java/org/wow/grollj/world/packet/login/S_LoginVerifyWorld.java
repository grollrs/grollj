package org.wow.grollj.world.packet.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.packet.SPacket;
import org.wow.grollj.world.packet.SPacketProducer;
import org.wow.grollj.world.packet.movement.C_SetActiveMover;
import org.wow.grollj.world.packet.movement.MoveFwd;

import java.util.Timer;
import java.util.TimerTask;

public class S_LoginVerifyWorld implements SPacket {

    int map;
    float x;
    float y;
    float z;
    float orientation;

    public S_LoginVerifyWorld(int map, float x, float y, float z, float orientation) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.z = z;
        this.orientation = orientation;
    }

    // SMSG_LOGIN_VERIFY_WORLD (0x236)

    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {

        session.getWorld().getCharacter().getPos().setO(orientation);

        // login verified
        System.out.println("Login verified: x: " + x);
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Starting movement");
                ctx.writeAndFlush(new C_SetActiveMover());
                ctx.writeAndFlush(new MoveFwd(session.getWorld().getCharacter().getPos(),session.getWorld().getServerTime().getCurrentServerTime()));
            }
        },1000);

    }

    public static SPacketProducer<S_LoginVerifyWorld> getProducer() {
        return new S_LoginVerifyWorld.S_LoginVerifyWorldProducer();
    }


    static class S_LoginVerifyWorldProducer implements SPacketProducer<S_LoginVerifyWorld> {

        /*
 734     WorldPacket data(SMSG_LOGIN_VERIFY_WORLD, 20);
 735     data << pCurrChar->GetMapId();
 736     data << pCurrChar->GetPositionX();
 737     data << pCurrChar->GetPositionY();
 738     data << pCurrChar->GetPositionZ();
 739     data << pCurrChar->GetOrientation();
 740     SendPacket(&data);
         */

        @Override
        public S_LoginVerifyWorld produce(ByteBuf in) {
            int map = in.readIntLE();
            float x = in.readFloatLE();
            float y = in.readFloatLE();
            float z = in.readFloatLE();
            float orientation = in.readFloatLE();

            return new S_LoginVerifyWorld(map,x,y,z,orientation);

        }
    }
}

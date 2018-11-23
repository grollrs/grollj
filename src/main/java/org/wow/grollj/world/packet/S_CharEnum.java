package org.wow.grollj.world.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.auth.AuthSession;
import org.wow.grollj.auth.messages.SMessage;
import org.wow.grollj.world.WorldSession;
import sun.nio.cs.UTF_8;

import java.nio.charset.Charset;

public class S_CharEnum implements SPacket {

    int guid;
    String name;
    int lvl;
    int zone;
    int map;
    float x;
    float y;
    float z;

    public S_CharEnum(int guid, String name, int lvl, int zone, int map, float x, float y, float z) {
        this.guid = guid;
        this.name = name;
        this.lvl = lvl;
        this.zone = zone;
        this.map = map;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static SPacketProducer<S_CharEnum> getProducer() {
        return new S_CharEnum.S_CharEnumProducer();
    }

    @Override
    public void handle(WorldSession session, ChannelHandlerContext ctx) {
        System.out.println("Handling char enum");
        // join world
        ctx.writeAndFlush(new C_PlayerLogin());
    }

    static class S_CharEnumProducer implements SPacketProducer<S_CharEnum>{

        @Override
        public S_CharEnum produce(ByteBuf in) {

            S_CharEnum result = null;
            byte num = in.readByte();
            for (int i = 0; i< num; i++){
                // foreach
                System.out.print("[Char] ");
                int guid = (int) in.readLongLE();
                System.out.print("GUID: " + guid+ " ");
                int nameLen = in.bytesBefore((byte) 0x00);
                System.out.print("nameLen: "+nameLen+ " ");
                String name = in.readCharSequence(nameLen, Charset.defaultCharset()).toString();
                System.out.print("Name: "+name+ " ");
                in.readBytes(9); //discard terminator + 8 fields
                byte lvl = in.readByte();
                System.out.print("Lvl: "+lvl+ " ");
                int zone = in.readIntLE();
                int map = in.readIntLE();
                float x = in.readFloatLE();
                float y = in.readFloatLE();
                float z = in.readFloatLE();
                System.out.println("Pos: ["+x+", "+y+", "+z+"]");
                result = new S_CharEnum(guid,name,lvl,zone,map,x,y,z);
                //TODO handle multiple chars per account
            }

            return result;
        }
    }
}

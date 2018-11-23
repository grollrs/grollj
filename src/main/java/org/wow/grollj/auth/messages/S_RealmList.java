package org.wow.grollj.auth.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.wow.grollj.auth.AuthSession;
import org.wow.grollj.shared.Realm;

import java.util.LinkedList;

public class S_RealmList implements SMessage {

    Realm realm;

    public S_RealmList(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void handle(AuthSession session, ChannelHandlerContext ctx) {
        System.out.println("Handle S_RealmList");
        session.setRealm(realm);
        session.getWsm().schedule(session);

        //activate world con
    }

    public static SMessageProducer getProducer() {
        return new S_RealmList.S_RealmListProducer();
    }

    public static byte getOpcode() {
        return AuthOpcodes.S_REALM_LIST.getOpcode();
    }

    private static class S_RealmListProducer implements SMessageProducer {
        @Override
        public SMessage produce(ByteBuf in) {
            // 10                           cmd
            //    29 00                     pkt size
            //          00 00 00 00         ?
            //                      01 00   num realm
            //for every realm
            // 00                           type
            //    00                        lock
            //       02                     flags

            // ... 00                       nulldelimited name
            // ... 00                       nulldelimited addr
            // 00 00 00 00                  population
            // 01                           char count
            //    01                        timezone
            //       01                     realmid
            //endfor
            //          10 00               ?


            if(in.readableBytes()<3){
                return null;
            }

            byte cmd = in.readByte();
            int len1 = in.readByte();
            int len2 = in.readByte();
            int remainingSize = (len2<<8)+len1;
            System.out.println("Realmlist size: "+remainingSize);

            if(in.readableBytes()<remainingSize){
                in.resetReaderIndex();
                return null;
            }
            byte[] blob = new byte[4];
            in.readBytes(blob);

            int num1 = in.readByte();
            int num2 = in.readByte();
            int numTotal = (num2<<8)+num1;


            LinkedList<Realm> realmList = new LinkedList<>();

            //read all realms
            for(int i = 0 ; i < numTotal;i++){
                byte type = in.readByte();
                byte lock = in.readByte();
                byte flag = in.readByte();
                StringBuffer nameBuffer = new StringBuffer();
                byte b;
                while((b = in.readByte())!=0x00){
                    nameBuffer.append((char)b);
                }
                String name = nameBuffer.toString();
                System.out.println("Realm name: "+name);

                StringBuffer addrBuffer = new StringBuffer();
                while((b = in.readByte())!=0x00){
                    addrBuffer.append((char)b);
                }
                String addr = addrBuffer.toString();
                System.out.println("Realm addr: "+addr);

                byte[] populationBlob = new byte[4];
                in.readBytes(populationBlob);

                byte charCount = in.readByte();
                byte realmID= in.readByte();
                byte timezone= in.readByte();
                Realm realm = new Realm(addr,name);
                realmList.add(realm);
            }

            byte[] endBlob = new byte[2];
            in.readBytes(endBlob);

            if(realmList.isEmpty()){
                return null;
            }
            return new S_RealmList(realmList.get(0));
        }

    }
}

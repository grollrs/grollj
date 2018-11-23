package org.wow.grollj;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.wow.grollj.auth.AuthSession;
import org.wow.grollj.auth.AuthSocket;
import org.wow.grollj.world.WorldSession;
import org.wow.grollj.world.WorldSocketManager;

public class Application {

    public static void main(String[] args){
        System.out.println("Starting GrollJ...");

        EventLoopGroup workerGroup = new NioEventLoopGroup();


        WorldSocketManager wsm = WorldSocketManager.getInstance();
        wsm.init(workerGroup);
        wsm.start();

        AuthSocket as = new AuthSocket(workerGroup);

        // this sould be done for every account
        AuthSession authSession = new AuthSession("PLAYER","PLAYER",wsm);
        ChannelFuture closeFuture = as.start(authSession);

        // wait until closed
        try {
            closeFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

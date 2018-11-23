package org.wow.grollj.world;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.wow.grollj.world.codec.WorldDecoder;
import org.wow.grollj.world.codec.WorldEncoder;

public class WorldSocket {
    EventLoopGroup workerGroup;

    public WorldSocket(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public ChannelFuture start(WorldSession world){
        String host;
        int port;
        String addr = world.authSession.getRealm().getAddr();
        if(addr.contains(":")){
            String [] parts = addr.split(":");
            host= parts[0];
            String portString = parts[1];
            port = Integer.valueOf(portString);
        }
        else{
            host="127.0.0.1";
            port=1234;
        }

        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline().addFirst(new LoggingHandler());

                ch.pipeline().addLast(new WorldEncoder(world));
                ch.pipeline().addLast(new WorldDecoder(world));

                ch.pipeline().addLast(new WorldPacketHandler(world));

            }
        });


        try {
            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            return f.channel().closeFuture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }
}

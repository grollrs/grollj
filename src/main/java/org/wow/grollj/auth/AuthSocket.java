package org.wow.grollj.auth;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class AuthSocket {

    EventLoopGroup workerGroup;


    public AuthSocket(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public ChannelFuture start(AuthSession session){
        String host = "127.0.0.1";
        int port = 3724;


        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline().addFirst(new LoggingHandler());

                ch.pipeline().addLast(new AuthEncoder());
                ch.pipeline().addLast(new AuthDecoder());

                ch.pipeline().addLast(new AuthPacketHandler(session));
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

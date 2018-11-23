package org.wow.grollj.auth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.wow.grollj.auth.messages.*;

public class AuthPacketHandler extends SimpleChannelInboundHandler<SMessage>{

    AuthSession session;

    public AuthPacketHandler(AuthSession session){
        this.session = session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SMessage msg) throws Exception {

        System.out.println("Handling message in AuthPacketHandler");

        msg.handle(session,ctx);

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Ch active");
        ctx.channel().write(new C_AuthLogonChallenge());
        ctx.flush();
    }
}

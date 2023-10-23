package com.shu.iot.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class Dp2StrHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private static final Logger LOG = LoggerFactory.getLogger(Dp2StrHandler.class);
    private final Charset charset;

    public Dp2StrHandler(){
        this.charset = Charset.defaultCharset();
    }
    public Dp2StrHandler(Charset charset) {
        this.charset = charset;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        //继续传递数据给下一个Handler
        ctx.fireChannelRead(packet.content().toString(charset));
    }
}

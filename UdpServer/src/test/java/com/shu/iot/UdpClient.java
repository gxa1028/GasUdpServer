package com.shu.iot;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SocketUtils;

public class UdpClient {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8082"));

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpClientHandler());

            Channel ch = b.bind(0).sync().channel();

            // 将消息广播给UDP服务器
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("开始广播", CharsetUtil.UTF_8),
                    SocketUtils.socketAddress("127.0.0.1", PORT))).sync();

            // 等待channel关闭，如果Channel没在5秒钟之内关闭，则打印异常
            if (!ch.closeFuture().await(5000)) {
                //log.info("channel没在5秒内关闭!");
                System.out.println("channel没在5秒内关闭!");
            }
        } finally {
            group.shutdownGracefully();
            System.out.println("client close");
        }
    }
}

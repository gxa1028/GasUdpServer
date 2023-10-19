package com.shu.iot.server;

import com.shu.iot.handler.NettyUdpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.apache.logging.log4j.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class UDPServer {
    private static final Logger LOG = LoggerFactory.getLogger(UDPServer.class);

    private int port;
    private final EventLoopGroup bossLoopGroup = new NioEventLoopGroup();
    private Bootstrap serverBootstrap;

    public void init(int port) {
        LOG.info("UDPServer init");
        this.port =  port;
        //创建netty bootstrap 启动类
        serverBootstrap = new Bootstrap();
        //设置boostrap 的eventLoopGroup线程组
        serverBootstrap.group(bossLoopGroup)
                //设置NIO UDP连接通道
                .channel(NioDatagramChannel.class)
                //设置通道参数 SO_BROADCAST广播形式
                .option(ChannelOption.SO_BROADCAST, false)
                //设置接收缓冲区大小,可以不设置
                //.option(ChannelOption.SO_RCVBUF, 1024 * 1024)
                //设置处理类 装配流水线
                .handler(new NettyUdpHandler());
        LOG.info("UDPServer init success");
    }

    public void start() {
        LOG.info("UDPServer start");
        try{
            InetAddress localHost = InetAddress.getLocalHost();
            String hostAddress = localHost.getHostAddress();
            LOG.info("UDPServer bind ip and port:{}:{}",hostAddress,this.port);
            ChannelFuture future = serverBootstrap.bind(this.port).sync();
            if (!future.isSuccess()) {
                LOG.error("bootstrap bind fail port is " + this.port);
                throw new Exception(String.format("Fail to bind on [host = %s , port = %d].", hostAddress, this.port), future.cause());
            } else {
                LOG.info("bootstrap bind success ");
            }
        }catch (Exception e){
            LOG.error("UDPServer start error, error reason:{}",e.getMessage(),e);
        }
    }

    public void stop() {
        LOG.info("UDPServer stop");
        this.bossLoopGroup.shutdownGracefully();
        LOG.info("UDPServer bossLoopGroup shutdown success");
    }

}

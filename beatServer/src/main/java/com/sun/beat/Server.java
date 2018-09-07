package com.sun.beat;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

	private int port;

	public Server(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		NioEventLoopGroup boosGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();

		try {
			bootstrap.group(boosGroup, workGroup)
						.channel(NioServerSocketChannel.class)
						.localAddress(new InetSocketAddress(port))
						//日志框架
						.handler(new LoggingHandler(LogLevel.INFO))
						//初始化服务端可连接队列。128队列长度
						.option(ChannelOption.SO_BACKLOG, 128)
						//参数用于设置TCP连接 保持链接
						.childOption(ChannelOption.SO_KEEPALIVE, true)
						//禁用nagle算法
						.childOption(ChannelOption.TCP_NODELAY,true)
						.childHandler(new BeatServerInitializer());
			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			workGroup.shutdownGracefully().sync();
			boosGroup.shutdownGracefully().sync();
		}

	}
	
	public static void main(String[] args) throws Exception {
		new Server(9970).run();
	}
}

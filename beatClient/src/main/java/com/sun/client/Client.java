package com.sun.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Client {

	private int port;
	private String host;

	public Client(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public void run() throws Exception {
		NioEventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap
				.group(group)
				.channel(NioSocketChannel.class)
				//禁用nagle算法     Nagle算法就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new LoggingHandler(LogLevel.INFO))
				.handler(new BeatClientInitializer());
			ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception{
		new Client("127.0.0.1", 9970).run();
	}
}

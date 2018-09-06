package com.sun.echo;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Servcer {

	private int port;
	
	public Servcer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws Exception {
		new Servcer(8950).run();
	}

	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			serverBootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
					.childHandler(new ServerInitializer());
			ChannelFuture channelFuture = serverBootstrap.bind().sync();
			System.out.println(Servcer.class + " started and listen on " + channelFuture.channel().localAddress());
			channelFuture.channel().closeFuture().sync();
		} finally {
				group.shutdownGracefully().sync();
		}

	}
}

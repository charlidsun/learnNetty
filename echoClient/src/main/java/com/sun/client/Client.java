package com.sun.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	private int port;
	private String host ;
	
	public Client(String host,int port) {
		this.host = host;
		this.port = port;
	}
	

	public static void main(String[] args) throws Exception {
		new Client("127.0.0.1", 8950).run();
	}

	public void run() throws Exception {
		NioEventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(group).channel(NioSocketChannel.class)
					.remoteAddress(new InetSocketAddress(host, port)).handler(new ClientInitialize());
			
			ChannelFuture channelFuture = bootstrap.connect().sync();
			
			channelFuture.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}

	}

}

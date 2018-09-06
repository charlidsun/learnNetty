package com.sun.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientInitialize extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		System.out.println("connected...");
		ch.pipeline().addLast(new ClientHandler());
	}

}

package com.sun.beat;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class BeatServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		//5s内没有，则运行userEventTriggered
		ch.pipeline().addLast(new IdleStateHandler(5, 0, 0,TimeUnit.SECONDS));
		ch.pipeline().addLast("decoder",new StringDecoder());
		ch.pipeline().addLast("encoder",new StringEncoder());
		ch.pipeline().addLast(new BeatServerHandler());
	}
}

package com.sun.heart.server;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

public class HeartbeatInitializer extends ChannelInitializer<Channel>{

	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast(new IdleStateHandler(5, 0, 0,TimeUnit.SECONDS))
					//自定义解码器
					.addLast(new HeartbeatDecoder())
					//数据处理
					.addLast(new HeartBeatSimpleHandle());
	}

}

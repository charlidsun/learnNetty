package com.sun.heartclient.client;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

public class CustomerHandleInitializer extends ChannelInitializer<Channel>{

	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline()
			.addLast(new IdleStateHandler(0, 10, 0,TimeUnit.SECONDS))
            .addLast(new HeartbeatDecoder())
            .addLast(new EchoClientHandle());
	}

}

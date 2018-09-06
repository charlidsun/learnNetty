package com.sun.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		
		System.err.println("客户端接收的消息："+msg.toString(CharsetUtil.UTF_8));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//发送
		ctx.writeAndFlush(Unpooled.copiedBuffer("hi server",CharsetUtil.UTF_8));
		
	}

	
}

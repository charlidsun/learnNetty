package com.sun.heartclient.client;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class EchoClientHandle extends SimpleChannelInboundHandler<ByteBuf> {

	private final static Logger LOGGER = LoggerFactory.getLogger(EchoClientHandle.class);

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

			if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
				LOGGER.info("已经 10 秒没有发送信息！");
				// 向服务端发送消息
				CustomProtocol heartBeat = SpringBeanFactory.getBean("heartBeat", CustomProtocol.class);
				ctx.writeAndFlush(heartBeat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
			}
		}

		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("已经建立了联系。。");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 异常时断开连接
		LOGGER.error("客户端链接异常，已经断开");
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws Exception {

		LOGGER.info("客户端收到消息={}", in.toString(CharsetUtil.UTF_8));

	}

}

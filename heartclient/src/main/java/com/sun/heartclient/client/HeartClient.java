package com.sun.heartclient.client;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 客户端
 * 
 * @author Administrator
 *
 */
@Component
public class HeartClient {

	private static Logger logger = LoggerFactory.getLogger(HeartClient.class);

	private NioEventLoopGroup group = new NioEventLoopGroup();

	@Value("${netty.server.host}")
	private String host;
	@Value("${netty.server.port}")
	private int port;
	private SocketChannel channel;

	@PostConstruct
	public void start() throws InterruptedException {

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new CustomerHandleInitializer());
		ChannelFuture future = bootstrap.connect(host, port).sync();
		if (future.isSuccess())
			logger.info("客户端已经启动");
		channel = (SocketChannel) future.channel();
	}

	// 消亡
	@PreDestroy
	public void destory() {
		group.shutdownGracefully().syncUninterruptibly();
		logger.info("客户端关闭");
	}

	public void sendMsg(CustomProtocol customProtocol) {
		ChannelFuture future = channel.writeAndFlush(customProtocol);
		future.addListener((ChannelFutureListener) channelFuture -> logger.info("客户端手动发消息成功={}",
				JSON.toJSONString(customProtocol)));

	}
}

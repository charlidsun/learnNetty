package com.sun.heart.server;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 跟随springboot容器一起启动
 * @author Administrator
 *
 */
@Component
public class HeartServer {
	
	//日志
	private Logger logger = LoggerFactory.getLogger(HeartServer.class);

	//获取配置文件的地址和端口
	@Value("${netty.server.host}")
	private String host;
	@Value("${netty.server.port}")
	private int port;
	
	//定义2个线程组，一个处理链接，一个处理数据
	private NioEventLoopGroup boosGroup = new NioEventLoopGroup();
	private NioEventLoopGroup workGroup = new NioEventLoopGroup();
	
	//启动
	@PostConstruct
	public void start() throws InterruptedException {
		//定义server引导类
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(boosGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				//保持长连接
				.childOption(ChannelOption.SO_KEEPALIVE,true)
				.childHandler(new HeartbeatInitializer());
	
		ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(host, port)).sync();
		if (future.isSuccess()) logger.info("服务器启动");
		//future.channel().closeFuture().sync();
	}
	
	//消亡
	@PreDestroy
	public void destory() {
		boosGroup.shutdownGracefully().syncUninterruptibly();
		workGroup.shutdownGracefully().syncUninterruptibly();
		logger.info("服务器关闭");
	}

}

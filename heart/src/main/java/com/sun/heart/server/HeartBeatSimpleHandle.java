package com.sun.heart.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class HeartBeatSimpleHandle extends SimpleChannelInboundHandler<CustomProtocol>{

	//日志
	private final static Logger logger = LoggerFactory.getLogger(HeartBeatSimpleHandle.class);
	//??
    private static final ByteBuf HEART_BEAT =  Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(new CustomProtocol(123456L,"pong").toString(),CharsetUtil.UTF_8));

    //关闭链接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	NettySocketHolder.remove((NioSocketChannel)ctx.channel());
    }

	
    /**
     * 处理过来的链接
     */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, CustomProtocol customProtocol) throws Exception {
		
		logger.info("新来一条链接地址->");
		//保存链接地址
		NettySocketHolder.add(customProtocol.getId(),(NioSocketChannel)ctx.channel()) ;

	}
	
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
				
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt ;
            
            if (idleStateEvent.state() == IdleState.READER_IDLE){
                logger.info("已经5秒没有收到信息！");
                //向客户端发送消息
                ctx.writeAndFlush(HEART_BEAT).addListener(ChannelFutureListener.CLOSE_ON_FAILURE) ;
            }
            
            System.out.println(NettySocketHolder.getCount());
        }
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 异常时断开连接
		logger.error("客户端链接异常，已经断开");
		cause.printStackTrace();
		ctx.close();
	}
	
	

}

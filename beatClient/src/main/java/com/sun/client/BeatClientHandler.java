package com.sun.client;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class BeatClientHandler extends ChannelInboundHandlerAdapter {
	
	 private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",  
	            CharsetUtil.UTF_8));  
	      
	 private static final int TRY_TIMES = 3;  
	      
	 private int currentTime = 0;  
	      

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("激活时间是：" + new Date());
		System.out.println("客户端激活 channelActive");
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		 System.out.println("停止时间是："+new Date());  
	     System.out.println("客户端停止 channelInactive");  
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		 String message = (String) msg;  
	     System.out.println("服务器发来的消息" + message);  
	     if (message.equals("Heartbeat")) {  
	            ctx.write("has read message from server");  
	            ctx.flush();  
	     }  
	    ReferenceCountUtil.release(msg);  
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("循环触发时间："+new Date());  
        if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.WRITER_IDLE) {  
                if(currentTime <= TRY_TIMES){  
                    System.out.println("currentTime:"+currentTime);  
                    currentTime++;  
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());  
                }
            }
        }
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}

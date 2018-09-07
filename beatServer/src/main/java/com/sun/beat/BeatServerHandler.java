package com.sun.beat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class BeatServerHandler extends ChannelInboundHandlerAdapter{

    private int loss_connect_time = 0;  

	//链接
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		System.out.println(ctx.channel().remoteAddress()+"客户端发送过来的新数据:"+msg.toString());
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
		//是不是心跳
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {  
                loss_connect_time++;  
                System.out.println("5 秒没有接收到"+ctx.channel().remoteAddress()+"客户端的信息了");  
                if (loss_connect_time > 2) {  
                    System.out.println("关闭这个不活跃的channel");  
                    ctx.channel().close();  
                }  
            } else {  
                super.userEventTriggered(ctx, evt);  
            }  
		}
		
	}

	//异常
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
}

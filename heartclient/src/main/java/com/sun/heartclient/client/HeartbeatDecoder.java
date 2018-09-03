package com.sun.heartclient.client;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 自定义解码器，使用pojo
 * 
 * @author Administrator
 *
 */
public class HeartbeatDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		long id = in.readLong();
		byte[] bytes = new byte[in.readableBytes()];
		in.readBytes(bytes);
		String content = new String(bytes);

		CustomProtocol customProtocol = new CustomProtocol();
		customProtocol.setId(id);
		customProtocol.setContent(content);
		out.add(customProtocol);
	}

}

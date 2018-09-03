package com.sun.heart.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import org.apache.catalina.valves.RemoteAddrValve;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 处理客户端连接数量
 * 
 * @author Administrator
 *
 */
public class NettySocketHolder {

	private static final Map<Long, NioSocketChannel> MAP = new ConcurrentHashMap<>(16);

	// add
	public static void add(Long id, NioSocketChannel channel) {
		MAP.put(id, channel);
	}

	// get
	public static NioSocketChannel get(Long id) {
		return MAP.get(id);
	}

	// all
	public static Map<Long, NioSocketChannel> getMAP() {
		return MAP;
	}

	// remove,根据value找到key，然后移除
	public static void remove(NioSocketChannel channel) {
		MAP.entrySet().stream().filter(entry -> entry.getValue() == channel)
				.forEach(entry -> MAP.remove(entry.getKey()));

	}
}

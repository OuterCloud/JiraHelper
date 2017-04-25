package com.quality.protocol.test.udp;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.quality.protocol.message.codec.MessageCodecFactory;
import com.quality.protocol.util.ContextConstant;

public class UdpProtocolServerMultiPortTest {

	public static void main(String[] args) {
		// --------------------------------port1-----------------------------------
		IoConnector connector1 = new NioDatagramConnector();
		connector1.setConnectTimeoutMillis(3000);
		connector1.getFilterChain().addLast("codec", new ProtocolCodecFilter(
				new MessageCodecFactory(ContextConstant.MESSAGE_TYPE_CLIENT, Charset.forName("UTF-8"))));

		// 注册IoHandler，即指定客户器端的消息处理器
		connector1.setHandler(new ClientHandler("UDP客户端连接1"));
		ConnectFuture cf1 = connector1.connect(new InetSocketAddress("127.0.0.1", 6088));
		
		// --------------------------------port2-----------------------------------
				IoConnector connector2 = new NioDatagramConnector();
				connector2.setConnectTimeoutMillis(3000);
				connector2.getFilterChain().addLast("codec", new ProtocolCodecFilter(
						new MessageCodecFactory(ContextConstant.MESSAGE_TYPE_CLIENT, Charset.forName("UTF-8"))));

				// 注册IoHandler，即指定客户器端的消息处理器
				connector2.setHandler(new ClientHandler("UDP客户端连接2"));
				ConnectFuture cf2 = connector2.connect(new InetSocketAddress("127.0.0.1", 7088));
				
				cf1.awaitUninterruptibly();
				cf1.getSession().getCloseFuture().awaitUninterruptibly();
				cf2.awaitUninterruptibly();
				cf2.getSession().getCloseFuture().awaitUninterruptibly();
	}

}
package com.quality.protocol.test.udp;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.quality.protocol.message.codec.MessageCodecFactory;
import com.quality.protocol.util.ContextConstant;

public class UdpProtocolServerTest {

	public static void main(String[] args) {
		// Create UDP connector
		IoConnector connector = new NioDatagramConnector();

		connector.setConnectTimeoutMillis(3000);

		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
				new MessageCodecFactory(ContextConstant.MESSAGE_TYPE_CLIENT, Charset.forName("UTF-8"))));

		// 注册IoHandler，即指定客户器端的消息处理器
		connector.setHandler(new ClientHandler("UDP客户端连接"));

		// 连接到服务器
		// 该方法用于与Server端建立连接，第二个参数若不传递则使用本地的一个随机端口访问Server端
		// 该方法是异步执行的，且可以同时连接多个服务端
		ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 6088));
		cf.awaitUninterruptibly();
		System.out.println("Mina UDP Client is startup");
		cf.getSession().getCloseFuture().awaitUninterruptibly();
	}

}
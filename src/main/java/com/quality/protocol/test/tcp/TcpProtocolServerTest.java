package com.quality.protocol.test.tcp;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.quality.protocol.message.codec.MessageCodecFactory;
import com.quality.protocol.util.ContextConstant;

public class TcpProtocolServerTest {

	public static void main(String[] args) {
		// Create TCP/IP connector
		// NioSocketConnector功能类似于JDK中的Socket类，它也是非阻塞的读取数据
		IoConnector connector = new NioSocketConnector();

		connector.setConnectTimeoutMillis(3000);

//		connector.getFilterChain().addLast("codec",
//				 new ProtocolCodecFilter(new
//				 TextLineCodecFactory(Charset.forName("UTF-8"))));
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
				new MessageCodecFactory(ContextConstant.MESSAGE_TYPE_CLIENT, Charset.forName("UTF-8"))));

		// 注册IoHandler，即指定客户器端的消息处理器
		connector.setHandler(new ClientHandler("TCP客户端连接"));

		// 连接到服务器
		// 该方法用于与Server端建立连接，第二个参数若不传递则使用本地的一个随机端口访问Server端
		// 该方法是异步执行的，且可以同时连接多个服务端
		ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 6488));
		cf.awaitUninterruptibly();
		System.out.println("Mina TCP Client is startup");
		cf.getSession().getCloseFuture().awaitUninterruptibly();
	}

}
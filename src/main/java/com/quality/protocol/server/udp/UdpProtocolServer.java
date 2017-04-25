package com.quality.protocol.server.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netease.common.util.StringUtil;
import com.quality.constant.LogConstant;
import com.quality.protocol.message.codec.MessageCodecFactory;
import com.quality.protocol.server.ProtocolServer;
import com.quality.protocol.util.ContextConstant;
import com.quality.protocol.util.LoadProtocolConfig;

@Service("udpProtocolServer")
public class UdpProtocolServer implements ProtocolServer {

	public static String charsetName = "UTF-8";
	public static InetSocketAddress socketAddress = null;
	public static NioDatagramAcceptor acceptor = null;

	@Autowired
	private ServerHandler serverHandler;
	@Override
	public void start() {
		// 从配置表中取出UDP的配置信息
		String port = null;
		List<Integer> intPort = new ArrayList<Integer>();
		try {
			port = LoadProtocolConfig.getProtocolConfig(ContextConstant.PROTOCOL_UDP_PORT);
			charsetName = LoadProtocolConfig.getProtocolConfig(ContextConstant.PROTOCOL_UDP_CHARSET);
		} catch (IOException e) {
			LogConstant.debugLog.info("加载protocol.properties文件时异常：", e);
		}
		if (StringUtil.isEmpty(port)) {
			LogConstant.runLog.info("protocol.properties文件中没有关于UDP端口port的配置！");
			LogConstant.runLog.info("UdpProtocolServer服务启动失败！");
			return;
		} else {
			try{
				String[] ports = port.split(ContextConstant.PORT_SPLIT);
				for(String aPort : ports){
					intPort.add(Integer.parseInt(aPort.trim()));
				}
			}catch(NumberFormatException e){
				LogConstant.debugLog.info("UdpProtocolServer服务端口转换异常：" + e);
				return;
			}
		}

		LogConstant.runLog.info("===========================UdpProtocolServer服务开始启动===========================");

		if (acceptor == null) {
			acceptor = new NioDatagramAcceptor();
		}

		// 获得IoSessionConfig对象
		DatagramSessionConfig cfg = acceptor.getSessionConfig();
		// 读写通道10秒内无操作进入空闲状态
		cfg.setIdleTime(IdleStatus.BOTH_IDLE, 40000);
		cfg.setReadBufferSize(2048 * 1024);// 发送缓冲区10M
		cfg.setReuseAddress(true);// 设置每一个非主监听连接的端口可以重用

		// 添加过滤器:日志
		LoggingFilter lf = new LoggingFilter();
		lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
		acceptor.getFilterChain().addLast("logger", lf);

		// 添加过滤器:编码解码器
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
				new MessageCodecFactory(ContextConstant.MESSAGE_TYPE_SERVER, Charset.forName(charsetName))));

		LogConstant.runLog.info("服务添加过滤器成功！");

		// 添加逻辑处理器
		acceptor.setHandler(serverHandler);
		LogConstant.runLog.info("服务添加Handler处理器成功！");
		// 绑定端口
		try {
			for(int aPort : intPort){
				socketAddress = new InetSocketAddress(aPort);
				acceptor.bind(socketAddress);
			}
			LogConstant.runLog.info("服务绑定端口成功！");
		} catch (IOException e) {
			LogConstant.debugLog.info("UdpProtocolServer服务绑定端口时异常：", e);
			return;
		}

		LogConstant.runLog.info("服务监听的端口为：" + port + "   编码方式为：" + charsetName);
		LogConstant.runLog.info("===========================UdpProtocolServer服务启动成功！===========================");
	}

	@Override
	public void stop() {
		if (null != acceptor) {
			acceptor.unbind(socketAddress);
			acceptor.getFilterChain().clear();
			acceptor.dispose();
			acceptor = null;
			LogConstant.runLog.info("UdpProtocolServer服务关闭成功！");
		}
	}

}
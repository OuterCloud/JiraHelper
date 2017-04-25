package com.quality.protocol.server.udp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quality.protocol.server.RequestMessageDispatcher;

/**
 * 逻辑处理器
 * 
 */
@Service("udpServerHandler")
public class ServerHandler extends IoHandlerAdapter {
	private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	@Autowired
	private RequestMessageDispatcher udpRequestMessageDispatcher;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
		super.exceptionCaught(session, cause);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		session.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// 使用线程池来处理
		if (cachedThreadPool != null)
			cachedThreadPool.execute(new ServerHandlerThread(session, message, udpRequestMessageDispatcher));
	}
}
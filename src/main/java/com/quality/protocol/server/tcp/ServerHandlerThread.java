package com.quality.protocol.server.tcp;

import org.apache.mina.core.session.IoSession;

import com.quality.constant.LogConstant;
import com.quality.protocol.message.RequestMessage;
import com.quality.protocol.message.ResponseMessage;
import com.quality.protocol.plugin.PluginFactory;
import com.quality.protocol.server.RequestMessageDispatcher;

public class ServerHandlerThread implements Runnable {

	private IoSession session;
	private Object message;
	private RequestMessageDispatcher tcpRequestMessageDispatcher;

	public ServerHandlerThread() {
	}

	public ServerHandlerThread(IoSession session, Object message,
			RequestMessageDispatcher tcpRequestMessageDispatcher) {
		this.message = message;
		this.session = session;
		this.tcpRequestMessageDispatcher = tcpRequestMessageDispatcher;
	}

	@Override
	public void run() {
		if (message instanceof RequestMessage) {
			RequestMessage request = (RequestMessage) message;
			LogConstant.runLog.info("[" + session.getRemoteAddress() + "] TCP请求的内容为：" + request.getRequest());
			String responseMes = tcpRequestMessageDispatcher.dispatch(session, request);
			responseMes = PluginFactory.renderFilterPlugin(request.getRequest(), responseMes);
			LogConstant.runLog.info("[" + session.getRemoteAddress() + "] TCP请求处理后的响应的内容为：" + responseMes);
			ResponseMessage response = new ResponseMessage();
			response.setResponse(responseMes);
			LogConstant.runLog.info("[" + session.getRemoteAddress() + "] TCP过滤器处理后的响应的内容为：" + responseMes);
			session.write(response);
		} else {
			LogConstant.debugLog.error("[" + session.getRemoteAddress() + "] TCP请求无效，请求内容为：" + message.toString());
		}
	}

}
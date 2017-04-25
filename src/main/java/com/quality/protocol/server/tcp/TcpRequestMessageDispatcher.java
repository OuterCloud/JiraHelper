package com.quality.protocol.server.tcp;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

import com.quality.protocol.message.RequestMessage;
import com.quality.protocol.server.RequestMessageDispatcher;

/**
 * @author li_zhe 
 * 处理TCP请求的分发，解析req中标识的类型将请求分发给对应的service处理返回具体的mock数据
 */
@Service("tcpRequestMessageDispatcher")
public class TcpRequestMessageDispatcher implements RequestMessageDispatcher {

	@Override
	public synchronized String dispatch(IoSession session, RequestMessage req) {
		String response = req.getRequest();
		return response;
	}
}
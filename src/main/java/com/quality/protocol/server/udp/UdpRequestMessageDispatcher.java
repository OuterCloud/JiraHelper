package com.quality.protocol.server.udp;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

import com.quality.protocol.message.RequestMessage;
import com.quality.protocol.server.RequestMessageDispatcher;

@Service("udpRequestMessageDispatcher")
public class UdpRequestMessageDispatcher implements RequestMessageDispatcher {

	@Override
	public synchronized String dispatch(IoSession session, RequestMessage req) {
		String response = req.getRequest();
		return response;
	}

}
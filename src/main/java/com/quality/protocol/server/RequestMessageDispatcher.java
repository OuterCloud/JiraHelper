package com.quality.protocol.server;

import org.apache.mina.core.session.IoSession;

import com.quality.protocol.message.RequestMessage;

public interface RequestMessageDispatcher {
	String dispatch(IoSession session, RequestMessage req);
}
package com.quality.protocol.message.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.quality.protocol.util.ContextConstant;


/**
 * 编解码过滤器工厂
 */
public class MessageCodecFactory implements ProtocolCodecFactory  {

	private MessageEncoder encoder;  
    private MessageDecoder decoder; 
    
	public MessageCodecFactory(String messageType, Charset charset) {
		if(ContextConstant.MESSAGE_TYPE_SERVER.equals(messageType)){
			this.encoder = new ResponseMessageEncoder(charset);
			this.decoder = new RequestMessageDecoder(charset);
		}else{
			this.encoder = new RequestMessageEncoder(charset);
			this.decoder = new ResponseMessageDecoder(charset);
		}
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}
}
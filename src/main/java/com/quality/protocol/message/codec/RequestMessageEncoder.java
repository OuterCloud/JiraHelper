package com.quality.protocol.message.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.quality.protocol.message.RequestMessage;
import com.quality.protocol.server.tcp.TcpProtocolServer;
import com.quality.protocol.util.DataUtil;

public class RequestMessageEncoder extends MessageEncoder  {

	private final Charset charset;
	public RequestMessageEncoder(){
		charset = Charset.forName(TcpProtocolServer.charsetName);
	}
	public RequestMessageEncoder(Charset charset){
		this.charset = charset;
	}
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		
		if(message instanceof RequestMessage){
			RequestMessage requst = (RequestMessage) message;
			IoBuffer buffer = IoBuffer.allocate(128).setAutoExpand(true);
			
			String req = requst.getRequest();
	        byte[] bytes = req.getBytes(charset);  
	        byte[] sizeBytes = DataUtil.intToBytes(bytes.length);
	          
	        buffer.put(sizeBytes);//将前4字节设置成数据体的字节长度  
	        buffer.put(bytes);//消息内容  
	        buffer.flip();
	        out.write(buffer);
		}
	}

}
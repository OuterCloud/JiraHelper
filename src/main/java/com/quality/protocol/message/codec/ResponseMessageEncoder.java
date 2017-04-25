package com.quality.protocol.message.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.quality.protocol.message.ResponseMessage;
import com.quality.protocol.server.tcp.TcpProtocolServer;
import com.quality.protocol.util.DataUtil;

public class ResponseMessageEncoder extends MessageEncoder {

	private final Charset charset;

	public ResponseMessageEncoder() {
		charset = Charset.forName(TcpProtocolServer.charsetName);
	}

	public ResponseMessageEncoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

		if (message instanceof ResponseMessage) {
			ResponseMessage response = (ResponseMessage) message;
			IoBuffer buffer = IoBuffer.allocate(128).setAutoExpand(true);
			String res = response.getResponse();
			byte[] bytes = res.getBytes(charset);
			byte[] sizeBytes = DataUtil.intToBytes(bytes.length);// 4个字节，表示内容的长度

			buffer.put(sizeBytes);
			buffer.put(bytes);// 消息内容
			buffer.flip();
			out.write(buffer);
		}
	}

}
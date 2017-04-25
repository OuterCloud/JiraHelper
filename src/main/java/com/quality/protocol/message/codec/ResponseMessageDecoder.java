package com.quality.protocol.message.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.quality.protocol.message.ResponseMessage;
import com.quality.protocol.util.DataUtil;

public class ResponseMessageDecoder extends MessageDecoder {

	private final Charset charset;
	public ResponseMessageDecoder(Charset charset){
		this.charset = charset;
	}
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() > 0) {
			byte[] sizeBytes = new byte[4];
			in.mark();
			in.get(sizeBytes);
			int size = DataUtil.bytesToInt(sizeBytes);
			if (size > in.remaining()) {
				in.reset();
				return false;
			} else {
				byte[] bytes = new byte[size];
				in.get(bytes, 0, size);
				String res = new String(bytes, charset);
				if (null != res && res.length() > 0) {
					ResponseMessage response = new ResponseMessage();
					response.setResponse(res);
					out.write(response);
				}
				if (in.remaining() > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
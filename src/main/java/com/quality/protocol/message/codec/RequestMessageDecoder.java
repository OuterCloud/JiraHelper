package com.quality.protocol.message.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.quality.protocol.message.RequestMessage;
import com.quality.protocol.util.DataUtil;

public class RequestMessageDecoder extends MessageDecoder {

	private final Charset charset;

	public RequestMessageDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() > 0) {// 有数据时，读取4字节判断消息长度
			byte[] sizeBytes = new byte[4];
			in.mark();// 标记当前位置，以便reset
			in.get(sizeBytes);// 读取前4字节
			int size = DataUtil.bytesToInt(sizeBytes);
			// 如果消息内容的长度不够则直接返回true
			if (size > in.remaining()) {// 如果消息内容不够，则重置，相当于不读取size
				in.reset();
				return false;// 接收新数据，以拼凑成完整数据
			} else {
				byte[] bytes = new byte[size];
				in.get(bytes, 0, size);
				String req = new String(bytes, charset);
				if (null != req && req.length() > 0) {
					RequestMessage request = new RequestMessage();
					request.setRequest(req);
					out.write(request);
				}
				if (in.remaining() > 0) {// 如果读取内容后还粘了包，就让父类再解码一次
					return true;
				}
			}
		}
		return false;// 处理成功，让父类进行接收下个包
	}
}
package com.rds.platform.mina2.coder;


import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class HDecoder extends CumulativeProtocolDecoder {
	private final Charset charset;

	public HDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	protected boolean doDecode(IoSession arg0, IoBuffer message,
			ProtocolDecoderOutput arg2) throws Exception {
		CharsetDecoder cd = charset.newDecoder();
//		String ss = message.getString(2, cd);
//		System.out.println(ss);
		String msg = message.getString(cd);
//		System.out.println(msg);
		
//		PlayerAccount_Entity paEntity = new PlayerAccount_Entity(id,name,emailAdress,sex);
		
		arg2.write(msg);
		return true;
	}
}

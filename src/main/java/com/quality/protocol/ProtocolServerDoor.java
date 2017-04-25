package com.quality.protocol;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quality.constant.LogConstant;
import com.quality.protocol.server.ProtocolServer;
import com.quality.protocol.util.ContextConstant;
import com.quality.protocol.util.LoadProtocolConfig;

@Service
public class ProtocolServerDoor {

	@Autowired
	private ProtocolServer tcpProtocolServer;

	@Autowired
	private ProtocolServer udpProtocolServer;

	@Autowired
	private ProtocolServer ftpProtocolServer;

	@PostConstruct
	public void start() {
		String protocolSwitch = null;
		try {
			protocolSwitch = LoadProtocolConfig.getProtocolConfig(ContextConstant.PROTOCOL_GLOBAL_SWITCH);
			if (protocolSwitch != null && protocolSwitch.equalsIgnoreCase(ContextConstant.PROTOCOL_GLOBAL_SWITCH_ON)) {
				String tcp = LoadProtocolConfig.getProtocolConfig(ContextConstant.PROTOCOL_TCP_ENABLE);
				if (tcp != null && tcp.equalsIgnoreCase(ContextConstant.PROTOCOL_ENABLE)) {
					tcpProtocolServer.start();
				}
				String udp = LoadProtocolConfig.getProtocolConfig(ContextConstant.PROTOCOL_UDP_ENABLE);
				if (udp != null && udp.equalsIgnoreCase(ContextConstant.PROTOCOL_ENABLE)) {
					udpProtocolServer.start();
				}
				String ftp = LoadProtocolConfig.getProtocolConfig(ContextConstant.PROTOCOL_FTP_ENABLE);
				if (ftp != null && ftp.equalsIgnoreCase(ContextConstant.PROTOCOL_ENABLE)) {
					ftpProtocolServer.start();
				}
			}
		} catch (IOException e) {
			LogConstant.debugLog.info("ProtocolServerDoor加载protocol.properties文件时异常：", e);
		}

	}

}
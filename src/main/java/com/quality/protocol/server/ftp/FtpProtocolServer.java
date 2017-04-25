package com.quality.protocol.server.ftp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.springframework.stereotype.Service;

import com.netease.common.util.StringUtil;
import com.quality.constant.LogConstant;
import com.quality.protocol.server.ProtocolServer;
import com.quality.protocol.util.ContextConstant;
import com.quality.protocol.util.LoadProtocolConfig;

@Service("ftpProtocolServer")
public class FtpProtocolServer implements ProtocolServer {

	private FtpServer server = null;
	private String defaultPort = "21";
	
	@Override
	public void start() {
		FtpServerFactory serverFactory = new FtpServerFactory();
		String port = null;
		LogConstant.runLog.info("===========================FtpProtocolServer服务开始启动===========================");
		try {
			port = LoadProtocolConfig.getProtocolConfig(ContextConstant.PROTOCOL_FTP_PORT);
		} catch (IOException e) {
			LogConstant.debugLog.info("加载protocol.properties文件时异常：", e);
		}
		if(StringUtil.isEmpty(port)){
			port = defaultPort;
		}
		String[] ports = port.split(ContextConstant.PORT_SPLIT);
		for(String aPort : ports){
			try{
				ListenerFactory factory = new ListenerFactory();
				int intPort = Integer.parseInt(aPort.trim());
				factory.setPort(intPort);
				if(intPort == 21){ // 默认监听21端口, 必须用default
					serverFactory.addListener("default", factory.createListener());
				}else{
					serverFactory.addListener("default" + intPort, factory.createListener());
				}
			}catch(NumberFormatException e){
				LogConstant.debugLog.info("FtpProtocolServer服务端口转换异常：" + e);
				return;
			}
		}
		LogConstant.runLog.info("服务设置端口成功！");

		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		File file = new File(FtpProtocolServer.class.getResource(ContextConstant.PROTOCOL_FTP_USER_PROPERTIES).getFile());
		userManagerFactory.setFile(file);
		UserManager  userManager = userManagerFactory.createUserManager();
		if(userManager != null){
			try {
				String[] allUserNames = userManager.getAllUserNames();
				if(allUserNames.length > 0){
					LogConstant.runLog.info("FTP服务目前用户信息:");
				}
				for(String name : allUserNames){
					User user = userManager.getUserByName(name);
					LogConstant.runLog.info("用户名: " + user.getName() + "    访问根目录: " + user.getHomeDirectory());
					List<Authority> authorities = user.getAuthorities();
					String allAuthority = "";
					for(Authority authority : authorities){
						allAuthority += authority.getClass().getSimpleName() + "   ";
					}
					LogConstant.runLog.info("该用户拥有的权限: " + allAuthority);
				}
			} catch (FtpException e) {
				LogConstant.runLog.info("获取FTP用户信息时异常: " + e);
				return;
			}
			serverFactory.setUserManager(userManager);
			LogConstant.runLog.info("服务设置用户配置文件成功！");
		}else{
			LogConstant.runLog.info("服务加载UserManager为空！");
			return;
		}

		if(server == null){
			server = serverFactory.createServer();
			try {
				server.start();
			} catch (FtpException e) {
				e.printStackTrace();
			}
			LogConstant.runLog.info("服务监听的端口为：" + port);
			LogConstant.runLog.info("===========================FtpProtocolServer服务启动成功！===========================");
		}else{
			if(!server.isStopped()){
				LogConstant.runLog.info("FtpProtocolServer服务已经启动!");
			}
		}
	}

	@Override
	public void stop() {
		if(server != null && !server.isStopped()){
			server.stop();
			server = null;
			LogConstant.runLog.info("===========================FtpProtocolServer服务关闭成功！===========================");
		}
	}

}
package com.quality.protocol.test.ftp;

import java.io.File;
import java.util.Scanner;

import com.netease.common.util.StringUtil;
import com.quality.constant.LogConstant;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

public class FtpClient {

	public static void main(String[] args) {
		FTPClient client = new FTPClient();
		LogConstant.runLog.info("FTPClient服务开始启动!");
		boolean isLogin = false;
		boolean quit = false;
		Scanner in;
		printHelpInfo();
		while (!quit) {
			String currentDir = "";
			try {
				if(client.isConnected()){
					currentDir = client.currentDirectory();
				}
			} catch (Exception e) {
				LogConstant.runLog.info("FTPClient服务异常：" + e);
			}
			if(StringUtil.isEmpty(currentDir)){
				System.out.print("输入命令：");
			}else{
				System.out.print("输入命令 [" + currentDir + "]：");
			}
			in = new Scanner(System.in);
			String input = in.nextLine();
			String[] params = input.split(" ");
			switch(params[0]){
			case "login":
				if(isLogin){
					System.out.println("你已经登录了FTP服务器！");
					break;
				}
				System.out.print("请输入需要连接FTP服务器的Host：");
				String host = in.nextLine();
				System.out.print("请输入需要连接FTP服务器的端口号：");
				String port = in.nextLine();
				System.out.print("请输入用户名：");
				String username = in.nextLine();
				System.out.print("请输入登录密码：");
				String password = in.nextLine();
				try {
					if(StringUtil.isEmpty(port))
						port = "21";
					int intPort = Integer.parseInt(port);
					client.connect(host, intPort);
					client.login(username, password);
				} catch (Exception e) {
					if(e.getMessage().contains("Authentication failed")){
						try {
							client.disconnect(false);
						} catch (Exception ee) {
							LogConstant.runLog.info(ee);
						}
					}
					LogConstant.runLog.info("FTPClient服务登录异常：" + e);
					break;
				}
				isLogin = true;
				System.out.println("FTPClient客户端登录成功！");
				break;
			case "list": // 列出服务器上当前目录下的文件
				if(isLogin){
					FTPFile[] list;
					try {
						list = client.list();
						for(FTPFile f : list){
						    if(!f.getName().equals(".") && !f.getName().equals("..")){
						    	if(f.getType() == 0){
						    		 System.out.print("文件" + "\t");
						    	}else if(f.getType() == 1){
						    		System.out.print("目录" + "\t");
						    	}else if(f.getType() == 2){
						    		System.out.print("连接" + "\t");
						    	}
						        System.out.print(f.getName() + "\t");  
						        System.out.println(client.modifiedDate(f.getName()));  
						    }
						}
					} catch (Exception e) {
						LogConstant.runLog.info("FTPClient服务列出文件异常：" + e);
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "mkdir":
				if(isLogin){
					if(params.length < 2 || StringUtil.isEmpty(params[1])){
						System.out.println("请在命令后加上目录名！");
						break;
					}else{
						try {
							client.createDirectory(params[1]);
							System.out.println("目录创建成功！");
						} catch (Exception e) {
							LogConstant.runLog.info("FTPClient服务创建目录异常：" + e);
						}
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "cd":
				if(isLogin){
					if(params.length < 2 || StringUtil.isEmpty(params[1])){
						System.out.println("请在命令后加上目录名！");
						break;
					}else{
						try {
							client.changeDirectory(params[1]); 
						} catch (Exception e) {
							LogConstant.runLog.info("FTPClient服务创建目录异常：" + e);
						}
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "pwd":
				if(isLogin){
					try {
						System.out.println("当前目录：" + client.currentDirectory()); 
					} catch (Exception e) {
						LogConstant.runLog.info("FTPClient服务获取当前目录异常：" + e);
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "rmdir":
				if(isLogin){
					if(params.length < 2 || StringUtil.isEmpty(params[1])){
						System.out.println("请在命令后加上目录名！");
						break;
					}else{
					    try {
							client.deleteDirectory(params[1]);
							System.out.println("目录删除成功！");
						} catch (Exception e) {
							if(e.getMessage().contains("Can't remove directory")){
								System.out.println("不能删除当前目录");
							}else if(e.getMessage().contains("Not a valid directory")){
								System.out.println("目录不存在或目录的路径不正确！");
							}else{
								LogConstant.runLog.info("FTPClient服务删除目录异常：" + e);
							}
						}
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "upload": // 上传文件到当前目录
				if(isLogin){
					if(params.length < 2 || StringUtil.isEmpty(params[1])){
						System.out.println("请在命令后添加文件路径！");
						break;
					}else{
						File file = new File(params[1]);
						if(file.exists()){
							try{
								client.upload(file, new ProgressionListener());
							}catch(Exception e){
								LogConstant.runLog.info("FTPClient服务上传文件时异常：" + e);
								break;
							}
							System.out.println("上传文件到服务器当前目录成功！");
						}else{
							System.out.println("文件不存在，请填写正确的文件路径！是否未写文件后缀。");
						}
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "download": // 第一个参数为要下载的文件路径，第二个参数为本地存放路径
				if(isLogin){
					if(params.length < 3 || StringUtil.isEmpty(params[1]) || StringUtil.isEmpty(params[2])){
						System.out.println("缺少参数，格式为upload srcPath destpath .");
						break;
					}else{
						File file = new File(params[2]);
						if(!file.exists()){
							try{
								client.download(params[1], file, new ProgressionListener());
							}catch(Exception e){
								LogConstant.runLog.info("FTPClient服务下载文件时异常：" + e);
								break;
							}
							System.out.println("从服务器下载文件成功！");
						}else{
							System.out.println("文件已存在，请重新填写文件路径！");
						}
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "logout":
				if(isLogin){
					if(client != null && client.isConnected()){
						try {
							client.disconnect(true);
							isLogin = false;
						} catch (Exception e) {
							LogConstant.runLog.info("FTPClient服务注销用户异常：" + e);
						} 
					}
				}else{
					System.out.println("你还未登录FTP服务器，请先登录！");
				}
				break;
			case "quit":
				// 退出客户端
				in.close();
				if(client != null && client.isConnected()){
					try {
						client.disconnect(true);
						isLogin = false;
					} catch (Exception e) {
						LogConstant.runLog.info("FTPClient服务退出异常：" + e);
					} 
				}
				quit = true;
				break;
			case "help":
				printHelpInfo();
				break;
			}
		}
	}
	
	public static void printHelpInfo(){
		System.out.println("***************************************************************************");
		System.out.println("                            FTP客户端操作命令 ");
		System.out.println("");
		System.out.println("                      login:          登录FTP服务器");
		System.out.println("                      list:           列出服务器上文件");
		System.out.println("                      mkdir:          创建目录 ");
		System.out.println("                      cd:             改变目录");
		System.out.println("                      pwd:            当前目录");
		System.out.println("                      rmdir:          删除目录");
		System.out.println("                      upload:         上传文件到服务器");
		System.out.println("                      download:       从服务器下载文件");
		System.out.println("                      logout:         退出FTP登录");
		System.out.println("                      quit:           退出客户端");
		System.out.println("                      help:           显示命令");
		System.out.println("");
		System.out.println("***************************************************************************");
	}
}
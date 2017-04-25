package com.quality.protocol.test.ftp;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

/**
 * @author li_zhe
 * 文件传输进度监听
 */
public class ProgressionListener implements FTPDataTransferListener{

	private long num = 0;
	@Override
	public void aborted() {
		System.out.println("传输中断");  
	}

	@Override
	public void completed() {
		System.out.println("传输完成");  
	}

	@Override
	public void failed() {
		 System.out.println("传输失败"); 
	}

	@Override
	public void started() {
		System.out.println("传输开始"); 
	}

	@Override
	public void transferred(int paramInt) {
		num += paramInt;
		 System.out.println("已经传输: " + num/1024 + "KB"); 
	}

}
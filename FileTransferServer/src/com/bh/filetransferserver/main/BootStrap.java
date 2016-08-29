package com.bh.filetransferserver.main;

import com.bh.filetransferserver.config.SystemConfig;
import com.bh.filetransferserver.server.FileServer;

/**
 * 
 * @author Hannibal
 *	文件传输服务端启动类
 */
public class BootStrap {
	public static void main(String[] args) {
		init();
	}
	
	private static void init() {
		//初始化系统参数
		SystemConfig.initConfig();
		
		//启动文件服务器监听
		FileServer.getInstance().bind(SystemConfig.socket_port);
	}
}

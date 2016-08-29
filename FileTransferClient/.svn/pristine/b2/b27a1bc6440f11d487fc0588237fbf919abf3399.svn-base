package com.bh.filetransferclient.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Hannibal
 * 读取配置文件类，初始化连接服务器，端口，发送文件目录等
 */
public class SystemConfig {
	private static Logger logger = Logger.getLogger(SystemConfig.class);
	
	//发送文件目录
	public static String send_filepath = "/home/cookiedata";
	
	//socket文件服务器ip
	public static String socket_ip = "127.0.0.1";
	
	//socket监听端口
	public static int socket_port = 22222;
	
	//是否删除文件
	public static int delfile_flag = 0;
	
	//文件队列大小
	public static int filequeue_size = 4000;
	
	//文件传输结束标识
	public final static String FILE_TRANSFER_END_FLAG = "$$$$FILETRANSFEREND$$$$";
	
	//开始从流中读取数据
	public static String START_RFLAG = "START";
	
	//目录标识
	public final static String PATH_FLAG = "path";
	
	//文件标识
	public final static String FILE_FLAG = "file";
	
	//删除文件标识
	public final static String DELFILE_FALG = "1";
	
	//不删除文件标识
	public final static String NON_DELFILE_FALG = "0";
	
	public static void initConfig() {
		Properties pro = new Properties();
		
		try {
			logger.info(" === 开始读取配置文件...");
			
			File proFile = new File("FileTransferClient.properties");
			pro.load(new FileInputStream(proFile));
			
			send_filepath = pro.getProperty("send_filepath", "send_filepath");
			logger.info(" === 发送文件目录：" + send_filepath);
			socket_ip = pro.getProperty("socket_ip", "127.0.0.1");
			logger.info(" === 文件服务器ip：" + socket_ip);
			socket_port = Integer.parseInt(pro.getProperty("socket_port", "22222"));
			logger.info(" === 连接端口：" + socket_port);
			delfile_flag = Integer.parseInt(pro.getProperty("delfile_flag", "0"));
			logger.info(" === 是否删除文件标记：" + delfile_flag);
			filequeue_size = Integer.parseInt(pro.getProperty("filequeue_size", "4000"));
			logger.info(" === 文件队列大小：" + filequeue_size);
			
			logger.info(" === 结束读取配置文件...");
		} catch (Exception e) {
			logger.error(null, e);
		}
	}
}

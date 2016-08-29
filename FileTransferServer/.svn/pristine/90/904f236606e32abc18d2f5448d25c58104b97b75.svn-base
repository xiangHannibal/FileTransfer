package com.bh.filetransferserver.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @author Hannibal
 *	系统参数初始化类，初始化接收文件存放路径，socket端口。
 */
public class SystemConfig {
	private static Logger logger = Logger.getLogger(SystemConfig.class);
	
	//接收文件存放目录
	public static String receive_filepath = "/home/receivefile";
	
	//socket监听端口
	public static int socket_port = 22222;
	
	//第一次从流中读取文件
	public static String FIRST_RFLAG = "FIRST";
	
	//开始从流中读取数据
	public static String START_RFLAG = "START";
	
	//文件传输结束标识
	public final static String FILE_TRANSFER_END_FLAG = "$$$$FILETRANSFEREND$$$$";
	
	public static void initConfig() {
		Properties pro = new Properties();
		File proFile = null;
		try {
			logger.info(" === 开始初始化系统参数... ");
			
			proFile = new File("FileTransferServer.properties");
			pro.load(new FileInputStream(proFile));
			
			receive_filepath = pro.getProperty("receive_filepath", "/home/receivefile");
			logger.info(" === 接收文件存放路径：" + receive_filepath);
			//判断服务端接收文件存放路径是否存在，不存在创建
			File receivePath = new File(receive_filepath);
			if (!receivePath.exists()) {
				receivePath.mkdirs();
				logger.info(" === " + receive_filepath + "文件不存在，已创建");
			}
			
			socket_port = Integer.parseInt(pro.getProperty("socket_port", "22222"));
			logger.info(" === socket监听端口：" + socket_port);
			logger.info(" === 结束初始化系统参数... ");
		} catch (Exception e) {
			logger.error(e, e);
		}
	}
}

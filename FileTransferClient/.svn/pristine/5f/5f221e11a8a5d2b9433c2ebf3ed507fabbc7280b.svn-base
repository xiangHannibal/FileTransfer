package com.bh.filetransferclient.main;

import java.io.File;

import org.apache.log4j.Logger;

import com.bh.filetransferclient.config.SystemConfig;
import com.bh.filetransferclient.queue.FileQueue;
import com.bh.filetransferclient.thread.FileScanThread;
import com.bh.filetransferclient.thread.FileUploadThread;

/**
 * @author Hannibal
 * 服务传输客户端启动类
 *
 */
public class BootStrap {
	private static Logger logger = Logger.getLogger(BootStrap.class);
	
	private static long SLEEP_TIME = 3 * 1000;
	
	public static void main(String[] args) {
		
		dealArgs(args);
	}
	
	/**
	 * @param args
	 * 处理传入的参数
	 * 1 参数为空时读取配置FileTransferClient.properties的参数，扫描配置的文件夹
	 * 2 参数不为空，参数个数只能是2，3，5，第一个参数只能是path或者file
	 * 3 参数个数为2时，第一个参数只能是path 第二个参数是待扫描的文件夹，多个文件夹以“,”号隔开
	 * 4 参数个数为3时，第一参数是file，第二个参数是是要上传的文件的绝对路径，多个文件以“,”号隔开
	 * 		第三个参数为0或者1，代码第二个参数中的文件是否删除
	 * 5 参数个数为5时，前三个参数跟参数个数为3的格式一样，第四个参数是是要上传的文件的绝对路径，多个文件以“,”号隔开
	 * 		第五个参数为0或者1，代码第四个参数中的文件是否删除
	 */
	private static void dealArgs(String[] args) {
		//初始化系统参数
		SystemConfig.initConfig();
		
		//初始化文件队列
		FileQueue fileQueue = FileQueue.getintance();
		
		if (null == args || 0 == args.length) {//不传参数，读取配置文件的目录
			//启动文件扫描线程
			new FileScanThread(fileQueue).start();
			//启动文件上传线程
			new FileUploadThread(fileQueue, SystemConfig.DELFILE_FALG, SystemConfig.PATH_FLAG).start();
		} else {//读取传入的参数，根据参数初始化要上传的文件或者目录
			if (2 != args.length && 3 != args.length && 5 != args.length) {
				logger.error(" === 传入的参数个数不符合形式...");
				
				return ;
			}
			
			String fileFlag = args[0];
			if (!SystemConfig.PATH_FLAG.equalsIgnoreCase(fileFlag) 
					&& !SystemConfig.FILE_FLAG.equalsIgnoreCase(fileFlag)) {
				logger.error(" === 第一个参数应传入path或者file，标识后面的参数是目录还是文件...");
				
				return ;
			}
			
			String files_param1 = args[1];
			if (SystemConfig.PATH_FLAG.equalsIgnoreCase(fileFlag)) {//如果传入的参数是目录
				SystemConfig.send_filepath = files_param1;
				//启动文件扫描线程
				new FileScanThread(fileQueue).start();
				//启动文件上传线程
				new FileUploadThread(fileQueue, SystemConfig.DELFILE_FALG, SystemConfig.PATH_FLAG).start();
			} else { //传入的是文件
				String delflag_param1 = args[2];
				if (!SystemConfig.DELFILE_FALG.equals(delflag_param1)
						&& !SystemConfig.NON_DELFILE_FALG.equals(delflag_param1)) {
					logger.error(" === 第三个参数应传入0或者1，标识是否删除文件...");
					
					return ;
				}
				
				//处理第一批文件
				initFileQueue(fileQueue, files_param1);
				new FileUploadThread(fileQueue, delflag_param1, SystemConfig.FILE_FLAG).start();//上传第一批参数的文件
				
				if (5 == args.length) {//有第二批文件
					String files_param2 = args[3];
					String delflag_param2 = args[4];
					
					if (!SystemConfig.DELFILE_FALG.equals(delflag_param2)
							&& !SystemConfig.NON_DELFILE_FALG.equals(delflag_param2)) {
						logger.error(" === 第五个参数应传入0或者1，标识是否删除文件...");
						
						return ;
					}
					
					// 等待上一批参数中传入的文件队列为空后再传第二批文件
					boolean continueUpload = true;
					while (!fileQueue.isEmpty()) {
						try {
							Thread.sleep(SLEEP_TIME);
							continueUpload = true;
						} catch (InterruptedException e) {
							logger.error(null, e);
							continueUpload = false;
						}
					}
					if (continueUpload) {
						initFileQueue(fileQueue, files_param2);
						new FileUploadThread(fileQueue, delflag_param2, SystemConfig.FILE_FLAG).start();//上传第一对参数的文件
					}
				}
			}
		}
	}
	
	/**
	 * 根据传入的文件参数初始化文件队列
	 * @param fileQueue
	 * @param path
	 */
	private static void initFileQueue(FileQueue fileQueue, String path) {
		try {
			String[] paths = path.split(",");
			for (String p : paths) {
				File file = new File(p);
				fileQueue.add(file);
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
	}
}

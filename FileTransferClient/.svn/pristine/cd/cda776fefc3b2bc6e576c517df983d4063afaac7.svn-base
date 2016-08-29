package com.bh.filetransferclient.thread;

import org.apache.log4j.Logger;

import com.bh.filetransferclient.client.FileClient;
import com.bh.filetransferclient.config.SystemConfig;
import com.bh.filetransferclient.queue.FileQueue;

/**
 * @author Hannibal
 * 文件上传线程，从文件队列中读出文件上传到服务器
 *
 */
public class FileUploadThread extends Thread {
	private static Logger logger = Logger.getLogger(FileUploadThread.class);
	
	private static long SLEEP_TIME = 1 * 1000;
	
	private FileQueue fileQueue;
	
	private String del_flag;
	
	private String fileFlag;
	
	public FileUploadThread(FileQueue fileQueue, String del_flag, String fileFlag) {
		this.fileQueue = fileQueue;
		this.del_flag = del_flag;
		this.fileFlag = fileFlag;
	}

	@Override
	public void run() {
		while (true) {
			try {
				while (!fileQueue.isEmpty()) {
					FileClient.getInstance().connect(SystemConfig.socket_ip, SystemConfig.socket_port, fileQueue.get(), del_flag);
				}
			} catch (Exception e) {
				logger.error(null, e);
			} finally {
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					logger.error(null, e);
				}
			}
			
			if (SystemConfig.FILE_FLAG.equals(fileFlag)) {
				logger.info("此线程停止");
				break;//当传入的参数是文件时，上传完成后停止文件上传线程
			}
		}
	}
}

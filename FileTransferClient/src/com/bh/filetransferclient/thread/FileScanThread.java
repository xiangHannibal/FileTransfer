package com.bh.filetransferclient.thread;

import java.io.File;

import org.apache.log4j.Logger;

import com.bh.filetransferclient.config.SystemConfig;
import com.bh.filetransferclient.queue.FileQueue;

/**
 * @author Hannibal
 * 扫描待上传文件的文件夹，将文件夹下的文件放入文件队列
 *
 */
public class FileScanThread extends Thread {
	private static Logger logger = Logger.getLogger(FileScanThread.class);
	
	private static long SLEEP_TIME = 3 * 1000;//扫描文件夹线程沉睡时间
	
	private FileQueue fileQueue;
	
	public FileScanThread(FileQueue fileQueue) {
		this.fileQueue = fileQueue;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String[] paths = SystemConfig.send_filepath.split(",");
				for (String path : paths) {
					File dir = new File(path);
					
					if (!dir.isDirectory()) {
						logger.error(" === " + SystemConfig.send_filepath + " 不是一个目录");
						break ;
					}
					logger.info(" === 开始扫描目录：" + dir + "下的文件...");
					listAllFiles(dir);
					logger.info(" === 结束扫描目录：" + dir + "下的文件..." + " 文件总数：" + fileQueue.size());
				}
			} catch (Exception e) {
				logger.error(" === 文件夹不存在", e);
			} finally {
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					logger.error(null, e);
				}
				
			}
			
		}
	}
	
	private void listAllFiles(File file) {
		if (fileQueue.size() >= SystemConfig.filequeue_size) {//超出了文件队列设置的最大值
			return ;
		}
		
		if (file.isFile()) {
			fileQueue.add(file);
			
			return ;
		}
		
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				listAllFiles(f);
			}
		}
	}
}

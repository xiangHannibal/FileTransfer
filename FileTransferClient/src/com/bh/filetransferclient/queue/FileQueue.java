package com.bh.filetransferclient.queue;

import java.io.File;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

/**
 * @author Hannibal
 * 待上传的文件队列
 *
 */
public class FileQueue {
	private static Logger logger = Logger.getLogger(FileQueue.class);
	
	private static LinkedBlockingQueue<File> queue = null;

	public static int count = 0;
	private static FileQueue intance = null;
	public static int setinit = 10000;
	private static LinkedBlockingQueue<String> set = new LinkedBlockingQueue<String>(
			setinit);

	public static FileQueue getintance() {
		if (intance == null)
			intance = new FileQueue();
		return intance;
	}

	private FileQueue() {
		queue = new LinkedBlockingQueue<File>();
	}

	public synchronized boolean contain(File f) {
		if (set.contains(f.getAbsolutePath()))
			return true;
		else {
			if (set.size() >= setinit * 0.8) {
				clear(setinit / 2);
				set.clear();
			}
			set.add(f.getAbsolutePath());
			return false;
		}
	}

	public void clear(int num) {
		for (int i = 0; i < num; i++)
			set.remove();
	}

	public synchronized void add(File file) {
		if (!queue.contains(file)) {
			try {
				queue.put(file);
			} catch (InterruptedException e) {
				logger.error(null, e);
			}
			count++;
		}
	}

	public void bathAdd(List<File> files) {
		try {
			for (int i = 0; i < files.size(); i++) {
				if (!contain(files.get(i)))// 判断是否已加入队列
				{
					if (queue.offer(files.get(i)))
						count++;
				}
			}
		} catch (Exception e) {
			logger.error(null, e);
		}
	}

	public synchronized File get() {
		if (!queue.isEmpty())
			return queue.remove();
		else
			return null;
	}

	public int size() {
		if (queue == null)
			return 0;
		else
			return queue.size();
	}

	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}

	public static synchronized int count() {
		return count;
	}
}

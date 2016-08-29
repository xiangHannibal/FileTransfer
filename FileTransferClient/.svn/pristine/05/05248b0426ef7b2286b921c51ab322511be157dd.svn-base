package com.bh.filetransferclient.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import com.bh.filetransferclient.config.SystemConfig;

/**
 * @author Hannibal
 * 发送文件处理类，将文件夹下的文件依次上传，并接收服务端传回的数据
 *
 */
public class FileClientHandler extends ChannelHandlerAdapter {
	private static Logger logger = Logger.getLogger(FileClientHandler.class);
	
	private File file;
	private String del_flag;
	
	public FileClientHandler(File file, String del_flag) {
		this.file = file;
		this.del_flag = del_flag;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		try {
				logger.info(" === 开始传输文件：" + file.getName());
				RandomAccessFile randomAccessFile = null;
//					ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
//					byte[] buffer = null;  
				try {
					writeFileHead(SystemConfig.START_RFLAG, ctx);
					writeFileHead(file.getName(), ctx);
					writeFileHead(String.valueOf(file.length()), ctx);
					logger.info(" === 上传文件长度：" + file.length());
					ctx.write(Unpooled.copiedBuffer(SystemConfig.FILE_TRANSFER_END_FLAG.getBytes()));
					ctx.flush();
					randomAccessFile = new RandomAccessFile(file, "r");
					
					//写文件内容
//						FileRegion region = new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length());
//						ctx.writeAndFlush(region);
					int len = 0;
					byte[] bytes = new byte[1024];
		            while((len = randomAccessFile.read(bytes, 0, 1024)) != -1) {  
		            	byte[] tBytes = new byte[len];
		            	for (int i = 0; i < len; i++) {
		            		tBytes[i] = bytes[i];
		            	}
		            	ctx.write(Unpooled.copiedBuffer(tBytes));//AndFlush
		            	ctx.write(Unpooled.copiedBuffer(SystemConfig.FILE_TRANSFER_END_FLAG.getBytes()));//传输的字节必须要用Unpooled.copiedBuffer处理
		            	ctx.flush();
//			            	bos.write(bytes, 0, len);
		            }
//			            buffer = bos.toByteArray();
//			            bos.close();
		            
				} catch (Exception e) { 
					logger.error(null, e);
				} finally {
					randomAccessFile.close();
					logger.info(" === 结束传输文件：" + file.getName());
					if (SystemConfig.DELFILE_FALG.equals(del_flag)) {
						file.delete();
						logger.info(" === 删除文件：" + file.getName());
					}
				}
					
		} catch (Exception e) {
			logger.error(" === 文件传输错误 ...", e);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String body = (String) msg;
		logger.info(" === 从服务端传回的消息【" + body + "】");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}
	
	private void writeFileHead(String data, ChannelHandlerContext ctx) throws IOException {
		byte[] d_content = data.getBytes();
		byte[] d_len = i2b(d_content.length);
		
		ctx.write(Unpooled.copiedBuffer(d_len));//传输的字节必须要用Unpooled.copiedBuffer处理
		ctx.write(Unpooled.copiedBuffer(d_content));
	}
	
	private byte[] i2b(int iSource) {
		byte[] bLocalArr = new byte[4];
		for ( int i = 0; (i < 4) && (i < 4); i++) {  
            bLocalArr[i] = (byte)( iSource>>8*i & 0xFF );  
        }  
		
		return bLocalArr;
	}
	
}

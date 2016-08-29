package com.bh.filetransferserver.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.bh.filetransferserver.config.SystemConfig;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Hannibal
 * 文件接收处理类，提取文件名，生成文件
 *
 */
public class FileServerHandler extends ChannelHandlerAdapter {
	private static Logger logger = Logger.getLogger(FileServerHandler.class);
	
	private String R_FLAG; //是否第一次接收数据
	private String FILE_NAME = "DEFAULT"; //文件名
	private String STATE = SystemConfig.START_RFLAG; //文件传输标志
	private Long FILE_LENGTH = 0L; //文件总长度
	private Long READ_LENGTH = 0l; //文件已读总长度
	
	private FileOutputStream fos;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info(" === 有客户端连接");
		
		R_FLAG = SystemConfig.FIRST_RFLAG;
		READ_LENGTH = 0L;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		
		initFileReceiveState(buf);
		
		if (isStart()) {
			saveFile(ctx, buf);
		} else {
			logger.info(" === 无法传输数据");
		}
		
		releaseBuf(buf);
		closeConn(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error(cause, cause);
		
		try {
			fos.close();
		} catch (IOException e) {
			logger.error(e, e);
		}
		
		ctx.close();
	}
	
	private void releaseBuf(ByteBuf buf) {
		buf.clear();
		buf.release();
	}
	
	/**
	 * 判断文件传输是否开始
	 * @return
	 */
	private boolean isStart() {
		boolean flag = false;
		
		if (SystemConfig.START_RFLAG.equals(STATE)) {
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * 关闭此次的连接
	 * @param ctx
	 */
	private void closeConn(ChannelHandlerContext ctx) throws Exception{
		if (FILE_LENGTH.longValue() == READ_LENGTH.longValue()) {
			try {
				fos.close();
				logger.info(" === " + FILE_NAME + " 文件传输结束...");
			} catch (IOException e) {
				logger.error(e, e);
			}
			ctx.close();
		}
	}
	
	/**
	 * 存储文件
	 * @param ctx
	 * @param buf
	 * @throws Exception
	 */
	private void saveFile(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
		if (buf.isReadable()) {
			READ_LENGTH += buf.readableBytes();
			
			byte[] bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			
			fos.write(bytes);
			fos.flush();
		}
	}
	
	/**
	 * 向客户端回写数据
	 * @param ctx
	 * @param msg
	 */
//	private void writeMessageToClient(ChannelHandlerContext ctx, String msg) throws Exception {
//		ctx.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
//	}
	
	/**
	 * 将字节数组转换成整数
	 * @param b
	 * @return
	 */
	private int b2i (byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {  
            int shift = (8 * i);  
            value += (b[i] & 0xFF) << shift;  
        }  
		
		return value;
	}
	
	/**
	 * 读取流的头部信息
	 * @param in
	 * @return
	 */
	private String readFileHead(ByteBuf in) {
		byte[] stateBytes = new byte[4];
		in.readBytes(stateBytes);
		
		int stateLength = b2i(stateBytes);
		
		byte[] sendBytes = new byte[stateLength];
		in.readBytes(sendBytes);
		
		return new String(sendBytes);
	}
	
	
	/**
	 * 读取客户端传入的头部信息，获取文件名，状态，新建文件输出流
	 * @param in
	 * @throws Exception
	 */
	private void initFileReceiveState(ByteBuf in) throws Exception {
		if (SystemConfig.FIRST_RFLAG.equals(R_FLAG)) {
			STATE = readFileHead(in);
			FILE_NAME = readFileHead(in);
			FILE_LENGTH = Long.parseLong(readFileHead(in));
			logger.info(" === 传入的文件大小：" + FILE_LENGTH);
			
			fos = new FileOutputStream(SystemConfig.receive_filepath + File.separator + FILE_NAME);
			
			R_FLAG = "other";
			
			logger.info(" === 文件传输开始，此次传入的文件名：" + FILE_NAME);
		}
	}
}

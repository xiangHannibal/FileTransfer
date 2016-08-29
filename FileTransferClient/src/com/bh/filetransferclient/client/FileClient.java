package com.bh.filetransferclient.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.io.File;

import org.apache.log4j.Logger;

import com.bh.filetransferclient.config.SystemConfig;
import com.bh.filetransferclient.handler.FileClientHandler;


/**
 * 
 * @author Hannibal
 *	文件发送客户端
 */
public class FileClient {
	private static Logger logger = Logger.getLogger(FileClient.class);
	
	private static FileClient instance = new FileClient();
	
	private FileClient() {
	}
	
	public static FileClient getInstance() {
		if (null == instance) {
			instance = new FileClient();
		}
		
		return instance;
	}
	
	public void connect(String host, int port, File file, String del_flag) {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
			.handler(new FileChannelInitializer(file, del_flag));
			
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error(" === 文件服务器连接失败... ", e);
		} finally {
			group.shutdownGracefully();
		}
	}
	
	class FileChannelInitializer extends ChannelInitializer<SocketChannel> {
		
		private File file;
		
		private String del_flag;
		
		public FileChannelInitializer(File file, String del_flag) {
			this.file = file;
			this.del_flag = del_flag;
		} 
		
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ByteBuf delimiter = Unpooled.copiedBuffer(SystemConfig.FILE_TRANSFER_END_FLAG.getBytes());
			ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2024, delimiter));//必须设置，socket对传输的字符大小有限制
			ch.pipeline().addLast(new FileClientHandler(file, del_flag));
		}
	}
}

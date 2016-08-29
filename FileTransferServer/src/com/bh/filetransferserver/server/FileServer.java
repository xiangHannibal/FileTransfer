package com.bh.filetransferserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import org.apache.log4j.Logger;

import com.bh.filetransferserver.config.SystemConfig;
import com.bh.filetransferserver.handler.FileServerHandler;

/**
 *
 * @author Hannibal
 * @description 文件接收服务器接收文件处理类，启动socket监听
 * 启动处理文件传输的handler类
 *
 */
public class FileServer {
	private static Logger logger = Logger.getLogger(FileServer.class);
	
	private static FileServer instance = new FileServer();
	
	private FileServer() {
	}
	
	public static FileServer getInstance() {
		if (null == instance) {
			instance = new FileServer();
		}
		
		return instance;
	}
	
	/**
	 * 绑定端口，启动socket监听
	 * 启动文件传输的handler
	 * @param port 监听端口
	 */
	public void bind(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
			.option(ChannelOption.SO_BACKLOG, 10000).childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ByteBuf delimiter = Unpooled.copiedBuffer(SystemConfig.FILE_TRANSFER_END_FLAG.getBytes());
					ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2024, delimiter));//必须设置，socket对传输的字符大小有限制
					ch.pipeline().addLast(new FileServerHandler());
				}
			});
			
			ChannelFuture f = b.bind(port).sync();
			logger.info(" === 文件传输服务器启动监听端口：" + port);
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}

package controller.chat;

import controller.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author Erica
 * @date 2021/1/23 12:21
 * @description 多人聊天室
 */
public class ChatServer {


    public static void main(String[] args) {
        //创建两个线程组bossGroup和workerGroup, 含有的子线程NioEventLoop的个数默认为cpu核数的两倍
        // bossGroup只是处理连接请求 ,真正的和客户端业务处理，会交给workerGroup完成
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 加入解码器（转化为ASCII）
                            pipeline.addLast("decoder",new StringDecoder());
                            // 加入编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new ChatServerHandler());
                        }
                    });
            System.out.println("聊天室启动。。。");
            ChannelFuture cf = bootstrap.bind(9000).sync();
//            cf.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture cf) throws Exception {
//                    if (cf.isSuccess()) {
//                        System.out.println("监听端口9000成功");
//                    } else {
//                        System.out.println("监听端口9000失败");
//                    }
//                }
//            });
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}

package controller.chat;

import controller.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author Erica
 * @date 2021/1/23 13:28
 * @description netty
 */
public class ChatClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder",new StringDecoder());
                            pipeline.addLast("encoder",new StringEncoder());
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });
            System.out.println("netty client start。。。");
            // 客户端去连接服务端（服务端端口为9000）
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9000).sync();
            // 得到channel
            Channel channel = channelFuture.channel();
            System.out.println("=========== " + channel.remoteAddress() + "===========");
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                // 通过channel发送服务器端
                channel.writeAndFlush(msg);
            }
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}

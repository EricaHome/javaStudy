package controller.chat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.time.temporal.ValueRange;
import java.util.Date;

/**
 * @author Erica
 * @date 2021/1/23 13:22
 * @description 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /* 表示channel处于就绪状态，提示上线*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 给channelGroup中现有的客户端发送消息，不包括当前客户端
        channelGroup.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 上线了 " + sdf.format(new Date()) + "\n");
        // 将当前客户端加入到channelGroup中
        channelGroup.add(channel);
        System.out.println(ctx.channel().remoteAddress() + " 上线了 " + "\n");
    }

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取当前channel
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 发送了消息： " + msg + "\n");
            } else {
                ch.writeAndFlush("[ 自己 ]发送了消息：" + msg +  "\n");
            }
        });
    }

    /* 表示channel处于不活动状态，提示离线了 */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[ 客户端 ]" + channel.remoteAddress() + " 下线了 " + "\n");
        System.out.println(ctx.channel().remoteAddress() + " 下线了 " + "\n");
        System.out.println("channelGroup size=" + channelGroup.size());
    }
}

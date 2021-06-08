//package com.wangzhen.services.student;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.websocket.server.ServerEndpoint;
//
///**
// * @Author wangzhen
// * @Description
// * @CreateDate 2020/3/25 12:29
// */
//@Component
//@ServerEndpoint("")
//public class NettyServer {
//    @Value("${server.port}")
//    private int port;
//    @PostConstruct
//    public void start() throws InterruptedException {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            ServerBootstrap sb = new ServerBootstrap();
//            sb.option(ChannelOption.SO_BACKLOG,1024);
//            sb.group(group,bossGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .localAddress(this.port)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            //初始化
//                            socketChannel.pipeline().addLast(new HttpServerCodec());
//                            socketChannel.pipeline().addLast(new ChunkedWriteHandler());
//                            socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));
//                            socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws","WebScoket",true,65536*10));
//                            socketChannel.pipeline().addLast(new MyWebSocketHandler());
//                        }
//                    });
//            ChannelFuture cf = sb.bind(this.port).sync();//服务器异步创建
//            cf.channel().closeFuture().sync();//关闭服务器通道
//        }finally {
//            group.shutdownGracefully().sync();
//            bossGroup.shutdownGracefully().sync();
//        }
//    }
//}

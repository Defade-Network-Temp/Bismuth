package fr.defade.bismuth.server;

import fr.defade.bismuth.core.utils.Utils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public class BismuthServer {
    private final static Logger LOGGER = LogManager.getLogger(BismuthServer.class);

    private final String host;
    private final int port;
    private final byte[] passwordHash;

    private ChannelFuture serverBootstrapFuture;

    public BismuthServer(String host, int port, byte[] passwordHash) {
        this.host = host;
        this.port = port;
        this.passwordHash = passwordHash;
    }

    public void bind() throws InterruptedException {
        serverBootstrapFuture = new ServerBootstrap()
                .group(Utils.generateNioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(host, port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {

                    }
                })
                .bind().sync();
    }

    public void stop() {
        if(serverBootstrapFuture != null) {
            try {
                serverBootstrapFuture.channel().close().sync();
            } catch (InterruptedException ignored) {
                LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }
}

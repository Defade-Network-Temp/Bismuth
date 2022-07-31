package fr.defade.bismuth.client;

import fr.defade.bismuth.core.utils.Utils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class BismuthClient {
    private static final Logger LOGGER = LogManager.getLogger(BismuthClient.class);

    private final String host;
    private final int port;
    private final byte[] password;

    private ChannelFuture clientBootstrapFuture;

    public BismuthClient(String host, int port, byte[] password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public void connect() throws InterruptedException {
        CompletableFuture<Void> connectFuture = new CompletableFuture<>();

        clientBootstrapFuture = new Bootstrap()
                .group(Utils.generateNioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {

                    }
                })
                .connect().sync();
    }

    public void disconnect() {
        if(clientBootstrapFuture != null) {
            try {
                clientBootstrapFuture.channel().close().sync();
            } catch (InterruptedException ignored) {
                LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }
}

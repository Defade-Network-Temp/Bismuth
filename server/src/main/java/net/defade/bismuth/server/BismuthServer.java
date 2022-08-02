package net.defade.bismuth.server;

import net.defade.bismuth.core.handlers.decoders.PacketDecoder;
import net.defade.bismuth.core.handlers.decoders.PacketLengthDecoder;
import net.defade.bismuth.core.handlers.encoders.PacketEncoder;
import net.defade.bismuth.core.handlers.encoders.PacketLengthEncoder;
import net.defade.bismuth.core.listeners.server.ServerPacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.protocol.PacketFlow;
import net.defade.bismuth.core.utils.Utils;
import net.defade.bismuth.server.handlers.ClientHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

public class BismuthServer {
    private final static Logger LOGGER = LogManager.getLogger(BismuthServer.class);

    private final KeyPair keyPair;
    private final String host;
    private final int port;
    private final byte[] passwordHash;
    private final Function<ConnectionProtocol, ServerPacketListener> packetListenersProvider;

    private ChannelFuture serverBootstrapFuture;

    public BismuthServer(String host, int port, byte[] passwordHash, Function<ConnectionProtocol, ServerPacketListener> packetListenersProvider) throws NoSuchAlgorithmException {
        this.keyPair = Utils.generateKeyPair();
        this.host = host;
        this.port = port;
        this.passwordHash = passwordHash;
        this.packetListenersProvider = packetListenersProvider;
    }

    public void bind() throws InterruptedException {
        serverBootstrapFuture = new ServerBootstrap()
                .group(Utils.generateNioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .localAddress(host, port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addFirst("prepender", new PacketLengthEncoder());
                        socketChannel.pipeline().addAfter("prepender", "encoder", new PacketEncoder(PacketFlow.CLIENTBOUND));

                        socketChannel.pipeline().addAfter("encoder", "splitter", new PacketLengthDecoder());
                        socketChannel.pipeline().addAfter("splitter", "decoder", new PacketDecoder(PacketFlow.SERVERBOUND));
                        socketChannel.pipeline().addAfter("decoder", "ClientHandler", new ClientHandler(keyPair, passwordHash, packetListenersProvider));
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

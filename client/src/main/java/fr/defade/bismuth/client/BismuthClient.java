package fr.defade.bismuth.client;

import fr.defade.bismuth.client.handlers.ServerHandler;
import fr.defade.bismuth.core.handlers.decoders.PacketDecoder;
import fr.defade.bismuth.core.handlers.decoders.PacketLengthDecoder;
import fr.defade.bismuth.core.handlers.encoders.PacketEncoder;
import fr.defade.bismuth.core.handlers.encoders.PacketLengthEncoder;
import fr.defade.bismuth.core.listeners.client.ClientPacketListener;
import fr.defade.bismuth.core.protocol.ConnectionProtocol;
import fr.defade.bismuth.core.protocol.PacketFlow;
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

    public CompletableFuture<Boolean> connect(ClientPacketListener clientPacketListener) {
        ConnectionProtocol connectionProtocol = ConnectionProtocol.getProtocolFromListener(clientPacketListener);
        if(connectionProtocol == ConnectionProtocol.LOGIN) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("PacketListener implements login listener"));
        } else if(connectionProtocol == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("PacketListener refers to no protocol"));
        }

        ServerHandler serverHandler = new ServerHandler(password, clientPacketListener);
        clientBootstrapFuture = new Bootstrap()
                .group(Utils.generateNioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addFirst("prepender", new PacketLengthEncoder());
                        socketChannel.pipeline().addAfter("prepender", "encoder", new PacketEncoder(PacketFlow.SERVERBOUND));

                        socketChannel.pipeline().addAfter("encoder", "splitter", new PacketLengthDecoder());
                        socketChannel.pipeline().addAfter("splitter", "decoder", new PacketDecoder(PacketFlow.CLIENTBOUND));
                        socketChannel.pipeline().addAfter("decoder", "ClientHandler", serverHandler);
                    }
                })
                .connect();

        return serverHandler.getConnectionFuture();
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

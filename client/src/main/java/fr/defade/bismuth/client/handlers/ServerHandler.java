package fr.defade.bismuth.client.handlers;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.listeners.client.ClientLoginPacketListener;
import fr.defade.bismuth.core.listeners.client.ClientPacketListener;
import fr.defade.bismuth.core.protocol.ConnectionProtocol;
import fr.defade.bismuth.core.protocol.packets.Packet;
import fr.defade.bismuth.core.protocol.packets.login.server.ServerboundClientProtocolPacket;
import fr.defade.bismuth.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;

public class ServerHandler extends SimpleChannelInboundHandler<Packet<? extends PacketListener>> {
    private final CompletableFuture<Boolean> connectionFuture = new CompletableFuture<>();
    private PacketListener clientPacketListener;

    private Channel serverChannel;

    public ServerHandler(byte[] password, ClientPacketListener apiProvidedClientPacketListener) {
        this.clientPacketListener = new ClientLoginPacketListener(connectionFuture, password);
        connectionFuture.whenComplete((isValidated, throwable) -> {
            if(throwable == null && isValidated) {
                ConnectionProtocol protocol = ConnectionProtocol.getProtocolFromListener(apiProvidedClientPacketListener);
                clientPacketListener.sendPacket(new ServerboundClientProtocolPacket(protocol));
                setProtocol(protocol);

                this.clientPacketListener = apiProvidedClientPacketListener;
                clientPacketListener.setChannel(serverChannel);
                clientPacketListener.channelActive();
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        serverChannel = ctx.channel();
        setProtocol(ConnectionProtocol.LOGIN);
        clientPacketListener.setChannel(serverChannel);
        clientPacketListener.channelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clientPacketListener.channelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        clientPacketListener.exceptionCaught(cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<? extends PacketListener> packet) throws Exception {
        Utils.packetGenericsHack(packet, clientPacketListener);
    }

    public void setProtocol(ConnectionProtocol protocol) {
        serverChannel.attr(ConnectionProtocol.CONNECTION_PROTOCOL_ATTRIBUTE_KEY).set(protocol);
    }

    public CompletableFuture<Boolean> getConnectionFuture() {
        return connectionFuture;
    }
}

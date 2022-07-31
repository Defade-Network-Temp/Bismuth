package fr.defade.bismuth.server.handlers;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.listeners.server.ServerLoginPacketListener;
import fr.defade.bismuth.core.listeners.server.ServerPacketListener;
import fr.defade.bismuth.core.protocol.ConnectionProtocol;
import fr.defade.bismuth.core.protocol.packets.Packet;
import fr.defade.bismuth.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.security.KeyPair;
import java.util.function.Function;

public class ClientHandler extends SimpleChannelInboundHandler<Packet<? extends PacketListener>> {
    private final Function<ConnectionProtocol, ServerPacketListener> packetListenersProvider;

    private Channel clientChannel;
    private PacketListener serverPacketListener;

    public ClientHandler(KeyPair keyPair, byte[] passwordHash, Function<ConnectionProtocol, ServerPacketListener> packetListenersProvider) {
        this.packetListenersProvider = packetListenersProvider;
        this.serverPacketListener = new ServerLoginPacketListener(keyPair, passwordHash);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clientChannel = ctx.channel();
        setProtocol(ConnectionProtocol.LOGIN);
        serverPacketListener.setChannel(clientChannel);
        serverPacketListener.channelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        serverPacketListener.channelInactive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        serverPacketListener.exceptionCaught(cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<? extends PacketListener> packet) throws Exception {
        Utils.packetGenericsHack(packet, serverPacketListener);
    }

    public void setProtocol(ConnectionProtocol protocol) {
        clientChannel.attr(ConnectionProtocol.CONNECTION_PROTOCOL_ATTRIBUTE_KEY).set(protocol);
    }
}

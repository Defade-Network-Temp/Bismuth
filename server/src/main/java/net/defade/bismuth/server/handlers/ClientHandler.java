package net.defade.bismuth.server.handlers;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.listeners.server.ServerLoginPacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.ServerInfosProvider;
import net.defade.bismuth.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.security.KeyPair;

public class ClientHandler extends SimpleChannelInboundHandler<Packet<? extends PacketListener>> {
    private Channel clientChannel;
    private PacketListener serverPacketListener;

    public ClientHandler(KeyPair keyPair, byte[] passwordHash, ServerInfosProvider serverInfosProvider) {
        this.serverPacketListener = new ServerLoginPacketListener((packetListener) -> {
            this.serverPacketListener = packetListener;
            serverPacketListener.setChannel(clientChannel);

            setProtocol(ConnectionProtocol.getProtocolFromListener(packetListener));
            serverPacketListener.channelActive();
        }, keyPair, passwordHash, serverInfosProvider);
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

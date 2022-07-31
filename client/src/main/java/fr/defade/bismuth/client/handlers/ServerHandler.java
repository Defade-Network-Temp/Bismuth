package fr.defade.bismuth.client.handlers;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.listeners.client.ClientLoginPacketListener;
import fr.defade.bismuth.core.listeners.client.ClientPacketListener;
import fr.defade.bismuth.core.protocol.ConnectionProtocol;
import fr.defade.bismuth.core.protocol.packets.Packet;
import fr.defade.bismuth.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Supplier;

public class ServerHandler extends SimpleChannelInboundHandler<Packet<? extends PacketListener>> {
    private Channel serverChannel;

    private final Supplier<ClientPacketListener> clientPacketListenerSupplier;
    private final PacketListener clientPacketListener;

    public ServerHandler(byte[] password, Supplier<ClientPacketListener> clientPacketListenerSupplier) {
        this.clientPacketListenerSupplier = clientPacketListenerSupplier;
        this.clientPacketListener = new ClientLoginPacketListener(password);
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
}

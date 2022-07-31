package fr.defade.bismuth.core.listeners;

import fr.defade.bismuth.core.protocol.packets.Packet;
import io.netty.channel.Channel;

public abstract class PacketListener {
    private Channel channel;

    public void channelActive() {

    }

    public void channelInactive() {

    }

    public void exceptionCaught(Throwable throwable) {

    }

    public void sendPacket(Packet<?> packet) {
        if(channel != null && channel.isOpen()) {
            channel.writeAndFlush(packet);
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

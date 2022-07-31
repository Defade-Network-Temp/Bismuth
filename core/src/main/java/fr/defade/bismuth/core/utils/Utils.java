package fr.defade.bismuth.core.utils;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.protocol.packets.Packet;
import io.netty.channel.nio.NioEventLoopGroup;

public class Utils {
    public static NioEventLoopGroup generateNioEventLoopGroup() {
        return new NioEventLoopGroup(new NettyThreadFactory());
    }

    // We can't simply call Packet#handle because our PacketListener might not be the right type for the diamond operator.
    // This method allows us to force the packet to handle it. This method might cause an exception, but if it does, it means that a
    // problem has happened before (ex: The client receives a packet which is made for the server).
    public static <T extends PacketListener> void packetGenericsHack(Packet<T> packet, PacketListener packetListener) {
        packet.handle((T) packetListener);
    }
}

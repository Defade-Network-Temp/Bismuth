package fr.defade.bismuth.core.handlers.encoders;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.protocol.ConnectionProtocol;
import fr.defade.bismuth.core.protocol.PacketFlow;
import fr.defade.bismuth.core.protocol.packets.Packet;
import fr.defade.bismuth.core.utils.BismuthByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;

public class PacketEncoder extends MessageToByteEncoder<Packet<? extends PacketListener>> {
    private final PacketFlow flow;

    public PacketEncoder(PacketFlow flow) {
        this.flow = flow;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet<? extends PacketListener> packet, ByteBuf out) throws Exception {
        ConnectionProtocol connectionProtocol = ctx.channel().attr(ConnectionProtocol.CONNECTION_PROTOCOL_ATTRIBUTE_KEY).get();

        if(connectionProtocol == null) {
            throw new NullPointerException("ConnectionProtocol is unknown: " + packet);
        }

        Integer packetId = connectionProtocol.getPacketId(flow, packet);
        if(packetId == null) {
            throw new IOException("Packet " + packet + " is unknown.");
        }

        BismuthByteBuf bismuthByteBuf = new BismuthByteBuf(out);
        bismuthByteBuf.writeInt(packetId);
        packet.write(bismuthByteBuf);
    }
}

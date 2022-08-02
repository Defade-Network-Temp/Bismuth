package net.defade.bismuth.core.handlers.decoders;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.protocol.PacketFlow;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<BismuthByteBuf> {
    private static final Logger LOGGER = LogManager.getLogger(PacketDecoder.class);

    private final PacketFlow flow;

    public PacketDecoder(PacketFlow flow) {
        this.flow = flow;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BismuthByteBuf byteBuf, List<Object> out) {
        int packetId = byteBuf.readInt();

        ConnectionProtocol connectionProtocol = ctx.channel().attr(ConnectionProtocol.CONNECTION_PROTOCOL_ATTRIBUTE_KEY).get();
        if(connectionProtocol == null) {
            throw new NullPointerException("ConnectionProtocol is unknown");
        }

        Packet<? extends PacketListener> packet = connectionProtocol.createPacket(flow, packetId, byteBuf);
        byteBuf.release();

        if(packet == null) {
            LOGGER.error("Received unknown packet with id " + packetId + " for protocol " + connectionProtocol);
            return;
        }

        out.add(packet);
    }
}

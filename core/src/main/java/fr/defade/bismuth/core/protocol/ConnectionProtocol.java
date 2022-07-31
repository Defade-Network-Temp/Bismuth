package fr.defade.bismuth.core.protocol;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.listeners.client.ClientLoginPacketListener;
import fr.defade.bismuth.core.protocol.packets.Packet;
import fr.defade.bismuth.core.utils.BismuthByteBuf;
import io.netty.util.AttributeKey;
import java.util.HashMap;
import java.util.Map;

public enum ConnectionProtocol {
    LOGIN(protocol()
            .addFlow(PacketFlow.CLIENTBOUND, new PacketSet<ClientLoginPacketListener>()

            )
    );

    private final Map<PacketFlow, PacketSet<?>> packets;

    ConnectionProtocol(ProtocolBuilder protocolBuilder) {
        this(protocolBuilder.build());
    }

    ConnectionProtocol(Map<PacketFlow, PacketSet<?>> packets) {
        this.packets = packets;
    }

    public Integer getPacketId(PacketFlow flow, Packet<? extends PacketListener> packet) {
        return packets.get(flow).getId(packet.getClass());
    }

    public Packet<? extends PacketListener> createPacket(PacketFlow flow, int packetId, BismuthByteBuf byteBuf) {
        return packets.get(flow).createPacket(packetId, byteBuf);
    }
    private static ProtocolBuilder protocol() {
        return new ProtocolBuilder();
    }

    private static class ProtocolBuilder {
        private final Map<PacketFlow, PacketSet<?>> packets = new HashMap<>();

        public <T extends PacketListener> ProtocolBuilder addFlow(PacketFlow side, PacketSet<T> packetSet) {
            packets.put(side, packetSet);
            return this;
        }

        public Map<PacketFlow, PacketSet<?>> build() {
            return packets;
        }
    }

    public static final AttributeKey<ConnectionProtocol> CONNECTION_PROTOCOL_ATTRIBUTE_KEY = AttributeKey.valueOf("connectionProtocol");
}

package net.defade.bismuth.core.protocol;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.listeners.client.ClientLoginPacketListener;
import net.defade.bismuth.core.listeners.client.YokuraClientPacketListener;
import net.defade.bismuth.core.listeners.server.ServerLoginPacketListener;
import net.defade.bismuth.core.listeners.server.YokuraServerPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundPasswordValidationPacket;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundRSAKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundAESKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundClientProtocolPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundPasswordPacket;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionProtocol {
    LOGIN(protocol()
            .addFlow(PacketFlow.CLIENTBOUND, new PacketSet<>(ClientLoginPacketListener.class)
                    .addPacket(ClientboundRSAKeyPacket.class, ClientboundRSAKeyPacket::new)
                    .addPacket(ClientboundPasswordValidationPacket.class, ClientboundPasswordValidationPacket::new)
            )
            .addFlow(PacketFlow.SERVERBOUND, new PacketSet<>(ServerLoginPacketListener.class)
                    .addPacket(ServerboundAESKeyPacket.class, ServerboundAESKeyPacket::new)
                    .addPacket(ServerboundPasswordPacket.class, ServerboundPasswordPacket::new)
                    .addPacket(ServerboundClientProtocolPacket.class, ServerboundClientProtocolPacket::new)
            )
    ),
    YOKURA(protocol()
            .addFlow(PacketFlow.CLIENTBOUND, new PacketSet<>(YokuraClientPacketListener.class)

            )
            .addFlow(PacketFlow.SERVERBOUND, new PacketSet<>(YokuraServerPacketListener.class)

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

    public static ConnectionProtocol getProtocolFromListener(PacketListener packetListener) {
        for(ConnectionProtocol protocols : values()) {
            for(PacketSet<?> packetSets : protocols.packets.values()) {
                if(packetSets.getPacketListenerClass().isAssignableFrom(packetListener.getClass())) {
                    return protocols;
                }
            }
        }

        return null;
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

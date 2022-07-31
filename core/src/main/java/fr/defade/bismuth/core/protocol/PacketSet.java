package fr.defade.bismuth.core.protocol;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.protocol.packets.Packet;
import fr.defade.bismuth.core.utils.BismuthByteBuf;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PacketSet<T extends PacketListener> {
    private final Class<T> packetListenerClass;
    private final Map<Integer, Function<BismuthByteBuf, Packet<T>>> idToDeserializer = new HashMap<>();
    private final Map<Class<? extends Packet<T>>, Integer> packetToId = new HashMap<>();

    public PacketSet(Class<T> packetListenerClass) {
        this.packetListenerClass = packetListenerClass;
    }

    public PacketSet<T> addPacket(Class<? extends Packet<T>> packet, Function<BismuthByteBuf, Packet<T>> deserializer) {
        int id = packetToId.size() + 1;

        packetToId.put(packet, id);
        idToDeserializer.put(id, deserializer);

        return this;
    }

    public Integer getId(Class<?> packet) {
        return packetToId.get(packet);
    }

    public Packet<T> createPacket(int id, BismuthByteBuf byteBuf) {
        if(id < 0 || id > idToDeserializer.size()) return null;

        Function<BismuthByteBuf, Packet<T>> deserializer = idToDeserializer.get(id);
        return deserializer != null ? deserializer.apply(byteBuf) : null;
    }

    public Class<T> getPacketListenerClass() {
        return packetListenerClass;
    }
}

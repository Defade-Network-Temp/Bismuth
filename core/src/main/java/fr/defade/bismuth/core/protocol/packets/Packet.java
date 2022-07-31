package fr.defade.bismuth.core.protocol.packets;

import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.utils.BismuthByteBuf;

public interface Packet<T extends PacketListener> {
    void write(BismuthByteBuf out);

    void handle(T packetListener);
}

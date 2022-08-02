package net.defade.bismuth.core.protocol.packets;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public interface Packet<T extends PacketListener> {
    void write(BismuthByteBuf out);

    void handle(T packetListener);
}

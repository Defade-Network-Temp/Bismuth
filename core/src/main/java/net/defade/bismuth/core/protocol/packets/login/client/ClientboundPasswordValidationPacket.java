package net.defade.bismuth.core.protocol.packets.login.client;

import net.defade.bismuth.core.listeners.client.ClientLoginPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ClientboundPasswordValidationPacket implements Packet<ClientLoginPacketListener> {
    private final boolean isPasswordValid;

    public ClientboundPasswordValidationPacket(boolean isPasswordValid) {
        this.isPasswordValid = isPasswordValid;
    }

    public ClientboundPasswordValidationPacket(BismuthByteBuf byteBuf) {
        this.isPasswordValid = byteBuf.readBoolean();
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeBoolean(isPasswordValid);
    }

    @Override
    public void handle(ClientLoginPacketListener packetListener) {
        packetListener.handlePasswordValidation(this);
    }

    public boolean isPasswordValid() {
        return isPasswordValid;
    }
}

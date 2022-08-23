package net.defade.bismuth.core.protocol.packets.yokura.server;

import net.defade.bismuth.core.listeners.server.YokuraServerPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.servers.ServerStatus;
import net.defade.bismuth.core.utils.BismuthByteBuf;

public class ServerboundUpdateServerStatusPacket implements Packet<YokuraServerPacketListener> {
    private final ServerStatus serverStatus;

    public ServerboundUpdateServerStatusPacket(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    public ServerboundUpdateServerStatusPacket(BismuthByteBuf byteBuf) {
        this.serverStatus = ServerStatus.valueOf(byteBuf.readUTF());
    }

    @Override
    public void write(BismuthByteBuf out) {
        out.writeUTF(serverStatus.toString());
    }

    @Override
    public void handle(YokuraServerPacketListener packetListener) {
        packetListener.updateServerStatus(this);
    }

    public ServerStatus getServerStatus() {
        return serverStatus;
    }
}

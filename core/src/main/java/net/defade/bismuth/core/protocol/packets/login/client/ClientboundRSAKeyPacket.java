package net.defade.bismuth.core.protocol.packets.login.client;

import net.defade.bismuth.core.listeners.client.ClientLoginPacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import net.defade.bismuth.core.utils.BismuthByteBuf;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class ClientboundRSAKeyPacket implements Packet<ClientLoginPacketListener> {
    private final PublicKey publicKey;

    public ClientboundRSAKeyPacket(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public ClientboundRSAKeyPacket(BismuthByteBuf byteBuf) {
        byte[] key = new byte[byteBuf.readInt()];
        byteBuf.readBytes(key);

        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void write(BismuthByteBuf out) {
        byte[] key = publicKey.getEncoded();
        out.writeInt(key.length);
        out.writeBytes(key);
    }

    @Override
    public void handle(ClientLoginPacketListener packetListener) {
        packetListener.handleRSAKey(this);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}

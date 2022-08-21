package net.defade.bismuth.core.listeners.server;

import net.defade.bismuth.core.handlers.decoders.CipherDecoder;
import net.defade.bismuth.core.handlers.encoders.CipherEncoder;
import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundPasswordValidationPacket;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundRSAKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundServerInfosPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundAESKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundClientProtocolPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundPasswordPacket;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import net.defade.bismuth.core.utils.ServerInfosProvider;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.function.Consumer;

public class ServerLoginPacketListener extends PacketListener {
    private final Consumer<ServerPacketListener> connectionSuccessfulRunnable;
    private final KeyPair keyPair;
    private final byte[] passwordHash;
    private final ServerInfosProvider serverInfosProvider;

    public ServerLoginPacketListener(Consumer<ServerPacketListener> connectionSuccessfulRunnable, KeyPair keyPair,
                                     byte[] passwordHash, ServerInfosProvider serverInfosProvider) {
        this.connectionSuccessfulRunnable = connectionSuccessfulRunnable;
        this.keyPair = keyPair;
        this.passwordHash = passwordHash;
        this.serverInfosProvider = serverInfosProvider;
    }

    @Override
    public void channelActive() {
        sendPacket(new ClientboundRSAKeyPacket(keyPair.getPublic()));
    }

    public void handleAESKey(ServerboundAESKeyPacket aesKeyPacket) {
        try {
            Cipher decrypt = Cipher.getInstance("RSA");
            decrypt.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            SecretKey aesKey = new SecretKeySpec(decrypt.doFinal(aesKeyPacket.getEncryptedAESKey()), "AES");

            getChannel().pipeline().addBefore("decoder", "decrypt", new CipherDecoder(aesKey));
            getChannel().pipeline().addAfter("prepender", "encrypt", new CipherEncoder(aesKey));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException exception) {
            throw new RuntimeException(exception); // TODO
        }
    }

    public void handlePassword(ServerboundPasswordPacket serverboundPasswordPacket) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(serverboundPasswordPacket.getPassword());

            if (Arrays.equals(passwordHash, hash)) {
                sendPacket(new ClientboundPasswordValidationPacket(true));
            } else {
                sendPacket(new ClientboundPasswordValidationPacket(false));
                getChannel().disconnect();
            }
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception); // TODO
        }
    }

    public void handleClientProtocol(ServerboundClientProtocolPacket clientProtocolPacket) {
        ServerPacketListener serverPacketListener = serverInfosProvider.getPacketListenerFromProtocol(clientProtocolPacket.getConnectionProtocol());

        serverPacketListener.readClientInfos(clientProtocolPacket.getClientInfos());
        BismuthByteBuf byteBuf = new BismuthByteBuf(getChannel().alloc().buffer());
        serverPacketListener.writeServerInfos(serverInfosProvider, byteBuf);
        sendPacket(new ClientboundServerInfosPacket(byteBuf));

        connectionSuccessfulRunnable.accept(serverPacketListener);
    }
}

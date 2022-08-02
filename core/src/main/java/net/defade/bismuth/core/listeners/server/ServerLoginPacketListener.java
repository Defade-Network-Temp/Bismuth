package net.defade.bismuth.core.listeners.server;

import net.defade.bismuth.core.handlers.decoders.CipherDecoder;
import net.defade.bismuth.core.handlers.encoders.CipherEncoder;
import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundPasswordValidationPacket;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundRSAKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundAESKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundClientProtocolPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundPasswordPacket;
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
    private final Consumer<ConnectionProtocol> connectionSuccessfulRunnable;
    private final KeyPair keyPair;
    private final byte[] passwordHash;

    public ServerLoginPacketListener(Consumer<ConnectionProtocol> connectionSuccessfulRunnable, KeyPair keyPair, byte[] passwordHash) {
        this.connectionSuccessfulRunnable = connectionSuccessfulRunnable;
        this.keyPair = keyPair;
        this.passwordHash = passwordHash;
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
        connectionSuccessfulRunnable.accept(clientProtocolPacket.getConnectionProtocol());
    }
}

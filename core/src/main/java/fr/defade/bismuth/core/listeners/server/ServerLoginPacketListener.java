package fr.defade.bismuth.core.listeners.server;

import fr.defade.bismuth.core.handlers.decoders.CipherDecoder;
import fr.defade.bismuth.core.handlers.encoders.CipherEncoder;
import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.protocol.packets.login.client.ClientboundPasswordValidationPacket;
import fr.defade.bismuth.core.protocol.packets.login.client.ClientboundRSAKeyPacket;
import fr.defade.bismuth.core.protocol.packets.login.server.ServerboundAESKeyPacket;
import fr.defade.bismuth.core.protocol.packets.login.server.ServerboundPasswordPacket;
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

public class ServerLoginPacketListener extends PacketListener {
    private final KeyPair keyPair;
    private final byte[] passwordHash;

    public ServerLoginPacketListener(KeyPair keyPair, byte[] passwordHash) {
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
}

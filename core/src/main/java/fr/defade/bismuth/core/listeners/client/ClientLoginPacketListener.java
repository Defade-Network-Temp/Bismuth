package fr.defade.bismuth.core.listeners.client;

import fr.defade.bismuth.core.handlers.decoders.CipherDecoder;
import fr.defade.bismuth.core.handlers.encoders.CipherEncoder;
import fr.defade.bismuth.core.listeners.PacketListener;
import fr.defade.bismuth.core.protocol.packets.login.client.ClientboundPasswordValidationPacket;
import fr.defade.bismuth.core.protocol.packets.login.client.ClientboundRSAKeyPacket;
import fr.defade.bismuth.core.protocol.packets.login.server.ServerboundAESKeyPacket;
import fr.defade.bismuth.core.protocol.packets.login.server.ServerboundPasswordPacket;
import fr.defade.bismuth.core.utils.Utils;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ClientLoginPacketListener extends PacketListener {
    private final byte[] password;

    public ClientLoginPacketListener(byte[] password) {
        this.password = password;
    }

    public void handleRSAKey(ClientboundRSAKeyPacket rsaKeyPacket) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPacket.getPublicKey());

            SecretKey aesKey = Utils.generateSecretKey();

            byte[] encryptedAESKey = cipher.doFinal(aesKey.getEncoded());
            sendPacket(new ServerboundAESKeyPacket(encryptedAESKey));

            getChannel().pipeline().addBefore("decoder", "decrypt", new CipherDecoder(aesKey));
            getChannel().pipeline().addAfter("prepender", "encrypt", new CipherEncoder(aesKey));

            sendPacket(new ServerboundPasswordPacket(password));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException exception) {
            throw new RuntimeException(exception); // TODO
        }
    }

    public void handlePasswordValidation(ClientboundPasswordValidationPacket passwordValidationPacket) {

    }
}

package net.defade.bismuth.core.listeners.client;

import io.netty.buffer.Unpooled;
import net.defade.bismuth.core.exceptions.DisconnectException;
import net.defade.bismuth.core.exceptions.PasswordInvalidException;
import net.defade.bismuth.core.handlers.decoders.CipherDecoder;
import net.defade.bismuth.core.handlers.encoders.CipherEncoder;
import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.protocol.ConnectionProtocol;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundPasswordValidationPacket;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundRSAKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.client.ClientboundServerInfosPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundAESKeyPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundClientProtocolPacket;
import net.defade.bismuth.core.protocol.packets.login.server.ServerboundPasswordPacket;
import net.defade.bismuth.core.utils.BismuthByteBuf;
import net.defade.bismuth.core.utils.Utils;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

public class ClientLoginPacketListener extends PacketListener {
    private final CompletableFuture<Void> connectionFuture;
    private final byte[] password;
    private final ClientPacketListener clientPacketListener;

    public ClientLoginPacketListener(CompletableFuture<Void> connectionFuture, byte[] password, ClientPacketListener clientPacketListener) {
        this.connectionFuture = connectionFuture;
        this.password = password;
        this.clientPacketListener = clientPacketListener;
    }

    @Override
    public void channelInactive() {
        connectionFuture.completeExceptionally(new DisconnectException());
    }

    @Override
    public void exceptionCaught(Throwable throwable) {
        connectionFuture.completeExceptionally(throwable);
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
        if(!passwordValidationPacket.isPasswordValid()) {
            connectionFuture.completeExceptionally(new PasswordInvalidException());
        } else {
            BismuthByteBuf clientInfos = new BismuthByteBuf(Unpooled.buffer());
            clientPacketListener.writeClientInfos(clientInfos);
            sendPacket(new ServerboundClientProtocolPacket(ConnectionProtocol.getProtocolFromListener(clientPacketListener), clientInfos));
        }
    }

    public void handleServerInfos(ClientboundServerInfosPacket serverInfosPacket) {
        clientPacketListener.readServerInfos(serverInfosPacket.getServerInfos());

        connectionFuture.complete(null);
    }
}

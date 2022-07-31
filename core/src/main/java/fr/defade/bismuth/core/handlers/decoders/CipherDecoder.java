package fr.defade.bismuth.core.handlers.decoders;

import fr.defade.bismuth.core.utils.BismuthByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CipherDecoder extends MessageToMessageDecoder<BismuthByteBuf> {
    private final Cipher cipher;

    public CipherDecoder(SecretKey key) {
        try {
            this.cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, BismuthByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] encryptedPacket = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(encryptedPacket);
        byteBuf.release();

        byte[] decryptedPacket = cipher.doFinal(encryptedPacket);
        BismuthByteBuf packetByteBuf = new BismuthByteBuf(Unpooled.buffer());
        packetByteBuf.writeBytes(decryptedPacket);

        list.add(packetByteBuf);
    }
}

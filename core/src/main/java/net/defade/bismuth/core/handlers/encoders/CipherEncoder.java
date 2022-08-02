package net.defade.bismuth.core.handlers.encoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {
    private final Cipher cipher;

    public CipherEncoder(SecretKey key) {
        try {
            this.cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException exception) {
            throw new RuntimeException(exception); // TODO
        }
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf message, ByteBuf out) throws IllegalBlockSizeException, BadPaddingException {
        byte[] packet = new byte[message.readableBytes()];
        message.readBytes(packet);

        byte[] encrypted = cipher.doFinal(packet);
        out.writeBytes(encrypted);
    }
}

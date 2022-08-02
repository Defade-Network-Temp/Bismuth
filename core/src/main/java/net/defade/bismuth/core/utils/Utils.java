package net.defade.bismuth.core.utils;

import net.defade.bismuth.core.listeners.PacketListener;
import net.defade.bismuth.core.protocol.packets.Packet;
import io.netty.channel.nio.NioEventLoopGroup;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static NioEventLoopGroup generateNioEventLoopGroup() {
        return new NioEventLoopGroup(new NettyThreadFactory());
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    // We can't simply call Packet#handle because our PacketListener might not be the right type for the diamond operator.
    // This method allows us to force the packet to handle it. This method might cause an exception, but if it does, it means that a
    // problem has happened before (ex: The client receives a packet which is made for the server).
    public static <T extends PacketListener> void packetGenericsHack(Packet<T> packet, PacketListener packetListener) {
        packet.handle((T) packetListener);
    }
}

package fr.defade.bismuth.core.handlers.encoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketLengthEncoder extends MessageToByteEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf message, ByteBuf out) {
        out.writeShort(message.readableBytes());
        out.writeBytes(message);
    }
}

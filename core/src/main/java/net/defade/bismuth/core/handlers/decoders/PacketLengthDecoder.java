package net.defade.bismuth.core.handlers.decoders;

import net.defade.bismuth.core.utils.BismuthByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketLengthDecoder extends ByteToMessageDecoder {
    private BismuthByteBuf packetBuffer;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        packetBuffer = new BismuthByteBuf(ctx.alloc().buffer());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        packetBuffer.writeBytes(byteBuf);

        while(packetBuffer.readableBytes() >= 4) {
            short packetSize = packetBuffer.readShort();
            if(packetBuffer.readableBytes() >= packetSize) {
                ByteBuf packetByteBuf = ctx.alloc().buffer(packetSize);
                packetBuffer.readBytes(packetByteBuf);

                out.add(new BismuthByteBuf(packetByteBuf));

                byte[] leftBytes = new byte[packetBuffer.readableBytes()];
                packetBuffer.readBytes(leftBytes);
                packetBuffer.release();

                packetBuffer = new BismuthByteBuf(ctx.alloc().buffer());
                packetBuffer.writeBytes(leftBytes);
            } else {
                packetBuffer.resetReaderIndex();
                break;
            }
        }
    }
}

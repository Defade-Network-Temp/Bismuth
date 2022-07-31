package fr.defade.bismuth.core.utils;

import io.netty.channel.nio.NioEventLoopGroup;

public class Utils {
    public static NioEventLoopGroup generateNioEventLoopGroup() {
        return new NioEventLoopGroup(new NettyThreadFactory());
    }
}

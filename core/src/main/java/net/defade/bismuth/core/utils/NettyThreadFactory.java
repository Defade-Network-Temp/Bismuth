package net.defade.bismuth.core.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyThreadFactory implements ThreadFactory {
    private static final AtomicInteger threadId = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Netty IO Thread #" + threadId.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}

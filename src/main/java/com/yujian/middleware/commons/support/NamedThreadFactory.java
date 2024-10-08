package com.yujian.middleware.commons.support;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private final AtomicInteger threadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemon;

    private final ThreadGroup group;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.getAndIncrement());
    }

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this(prefix, daemon, null);
    }

    public NamedThreadFactory(String prefix, boolean daemon, ThreadGroup group) {
        this.prefix = prefix + "-thread-";
        this.daemon = daemon;
        if (group != null) {
            this.group = group;
        } else {
            SecurityManager s = System.getSecurityManager();
            this.group = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = prefix + threadNum.getAndIncrement();
        Thread ret = new Thread(group, runnable, name, 0);
        ret.setDaemon(daemon);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return group;
    }
}
package igorekpotworek.infrastructure;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class Guard {

    private final Lock readLock;
    private final Lock writeLock;

    public Guard() {
        this(new ReentrantReadWriteLock());
    }

    public Guard(ReadWriteLock lock) {
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public <T> T read(Supplier<T> block) {
        readLock.lock();
        try {
            return block.get();
        } finally {
            readLock.unlock();
        }
    }

    public <T> T write(Supplier<T> block) {
        writeLock.lock();
        try {
            return block.get();
        } finally {
            writeLock.unlock();
        }
    }
}

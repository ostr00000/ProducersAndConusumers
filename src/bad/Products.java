package bad;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Products {
    private int bufferSize;
    private int usedBuffer = 0;

    private Lock lock = new ReentrantLock();
    private Condition allProducers = lock.newCondition();
    private Condition allConsumers = lock.newCondition();

    Products(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    void produce(int numberOfProducts, int id) {
        lock.lock();
        try {
            while (this.usedBuffer + numberOfProducts > this.bufferSize) {
                allProducers.await();
            }
            this.usedBuffer += numberOfProducts;
            //System.out.println("Produced: " + numberOfProducts + " by producer " + id);
            //System.out.println("Buffer: " + this.usedBuffer + "/" + this.bufferSize);
            allProducers.signal();
            allConsumers.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void consume(int numberOfProducts, int id) {
        lock.lock();
        try {
            int last_success = 0;
            while (this.usedBuffer - numberOfProducts < 0) {
                last_success += 1;
                allConsumers.await();
            }
            if (id == -1) {
                System.out.println("last success: " + last_success);
            }

            this.usedBuffer -= numberOfProducts;
            //System.out.println("Consumed: " + numberOfProducts + " by consumer " + id);
            //System.out.println("Buffer: " + this.usedBuffer + "/" + this.bufferSize);
            allProducers.signal();
            allConsumers.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

package good;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Products {
    private int bufferSize;
    private int usedBuffer = 0;

    private Boolean isFirstProducer = false;
    private Boolean isFirstConsumer = false;

    private ReentrantLock lock = new ReentrantLock();
    private Condition allProducers = lock.newCondition();
    private Condition firstProducer = lock.newCondition();
    private Condition allConsumers = lock.newCondition();
    private Condition firstConsumer = lock.newCondition();

    Products(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    void produce(int numberOfProducts, int id) {
        lock.lock();
        try {
            if (isFirstProducer) {
                allProducers.await();
            }
            isFirstProducer = true;

            while (this.usedBuffer + numberOfProducts > this.bufferSize) {
                firstProducer.await();
            }
            this.usedBuffer += numberOfProducts;
            //System.out.println("Produced: " + numberOfProducts + " by producer " + id);
            //System.out.println("Buffer: " + this.usedBuffer + "/" + this.bufferSize);

            if(!lock.hasWaiters(allProducers)){
                isFirstProducer = false;
            }
            firstConsumer.signal();
            allProducers.signal();

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
            if (isFirstConsumer) {
                allConsumers.await();
            }

            isFirstConsumer = true;

            while (this.usedBuffer - numberOfProducts < 0) {
                last_success += 1;
                firstConsumer.await();
            }

            if (id == -1) {
                System.out.println("last success: " + last_success);
            }

            this.usedBuffer -= numberOfProducts;
            //System.out.println("Consumed: " + numberOfProducts + " by consumer " + id);
            //System.out.println("Buffer: " + this.usedBuffer + "/" + this.bufferSize);

            if(!lock.hasWaiters(allConsumers)){
                isFirstConsumer = false;
            }
            firstProducer.signal();
            allConsumers.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

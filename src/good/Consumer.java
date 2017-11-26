package good;

import java.util.Random;

public class Consumer implements Runnable {
    private Products p;
    private int id;
    public int value = 1;

    Consumer(Products p, int id, int value) {
        this.p = p;
        this.id = id;
        this.value = value;
    }

    @Override
    public void run() {
        Random r = new Random();
        while (true) {
            int rand = value;// + r.nextInt(p.bufferSize / 2);
            //System.out.println("good.Consumer " + this.id + " want to consume " + rand + " units");
            p.consume(rand, this.id);
        }
    }
}

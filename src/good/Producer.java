package good;

import java.util.Random;

public class Producer implements Runnable {
    private Products p;
    private int id;

    Producer(Products p, int id) {
        this.p = p;
        this.id = id;
    }

    @Override
    public void run() {
        Random r = new Random();
        while (true) {
            int rand = 1;// + r.nextInt(p.bufferSize / 2);
            //System.out.println("good.Producer " + this.id + " want to produce " + rand + " units");
            p.produce(rand, this.id);
        }
    }
}

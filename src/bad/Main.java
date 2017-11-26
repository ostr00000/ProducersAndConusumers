package bad;

public class Main {

    public static void main(String[] args) {
        int bufferSize = 100;
        Products p = new Products(bufferSize);

        int cons = 70;
        int prod = 100;

        Thread threads[] = new Thread[cons + prod];
        for (int i = 0; i < cons; i++) {
            threads[i] = new Thread(new Consumer(p, i, bufferSize / 100), "cons" + i);

        }
        for (int i = cons; i < cons + prod; i++) {
            threads[i] = new Thread(new Producer(p, i), "prod" + i);
        }

        Consumer bigConsumer = new Consumer(p, -1, bufferSize);
        Thread tt = new Thread(bigConsumer, "bigCons");
        tt.start();

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

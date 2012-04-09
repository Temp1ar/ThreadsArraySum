package ru.spbau.korovin.arraysum;

public class Master implements Runnable {
    private final int WORKERS = 3;
    private final int[] chunk;
    private long[] workerResults = new long[WORKERS];
    private final int pieceSize;
    private long result = 0;

    public Master(int[] chunk) {
        this.chunk = chunk;
        pieceSize = (int) Math.round(Math.ceil(chunk.length / WORKERS));
    }

    public void run() {
        Thread p[] = new Thread[WORKERS];

        for(int i = 0; i < WORKERS; i++) {
            Worker w = new Worker(i);
            p[i] = new Thread(w);
            p[i].start();
        }

        try {
            for(int i = 0; i < WORKERS; i++) {
                p[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < WORKERS; i++) {
            result += workerResults[i];
        }
    }

    public long getResult() {
        return result;
    }

    private class Worker implements Runnable {
        private final int num;

        private Worker(int num) {
            this.num = num;
        }

        public void run() {
            long sum = 0;
            int last  = Math.min((num+1)*pieceSize, chunk.length);
            int first = Math.min(num*pieceSize, last);
            for(int i = first; i < last; i++) {
                sum += chunk[i];
            }

            workerResults[num] = sum;
        }
    }
}

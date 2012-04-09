import ru.spbau.korovin.arraysum.Master;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int MASTERS = 4;

    public static void main(String[] args) {
        int n;
        System.out.println("Enter the size of array:");
        Scanner s = new Scanner(System.in);
        n = s.nextInt();
        // Filling with zeros to next %12 equal n
        int zeros = 12 - (n % 12);

        int data[] = new int[n + zeros];
        Random random = new Random();
        for(int i = 0; i < n; i++) {
            data[i] = random.nextInt(100);
        }
        // Filling with zeros empty part of data array
        for(int i = n; i < n + zeros; i++) {
            data[i] = 0;
        }
        n += zeros;

        int chunks[][] = new int[MASTERS][n/MASTERS];
        for(int i = 0; i < n; i++) {
            chunks[i / (n/MASTERS)][i % (n/MASTERS)] = data[i];
        }

        Thread p[] = new Thread[MASTERS];
        Master master[] = new Master[MASTERS];
        for(int i = 0; i < MASTERS; i++) {
            master[i] = new Master(chunks[i]);
            p[i] = new Thread(master[i]);
            p[i].start();
        }

        try {
            for(int i = 0; i < MASTERS; i++) {
                p[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long result = 0;
        for(int i = 0; i < MASTERS; i++) {
            result += master[i].getResult();
        }

        System.out.println("Threaded sum result: " + result);

        long trueSum = 0;
        for (int i : data) {
            trueSum += i;
        }
        System.out.println("Simple sum result: " + trueSum);
    }
}

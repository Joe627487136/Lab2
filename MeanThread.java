package Lab2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Lab2.MeanThread.MyRunnable.doublemeanlist;

public class MeanThread {
    private static final String PATH = "/Users/zhouxuexuan/AndroidStudioProjects/Lab/lab/src/main/java/Lab2/input.txt";

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        ArrayList<Integer> array = new ArrayList<Integer>();
        ArrayList<Integer> subLists = new ArrayList<Integer>();
        ArrayList finalList = new ArrayList<ArrayList>();
        try {
            FileReader fr = new FileReader(PATH);
            BufferedReader txtReader = new BufferedReader(fr);
            String s = txtReader.readLine();
            for (String retval : s.split(" ")) {
                array.add(Integer.valueOf(retval));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Size:");
        System.out.println(array.size());
        int listSize = array.size();
        Scanner sc = new Scanner(System.in);
        System.out.print("Input chunk:\n");
        int chunks = sc.nextInt();
        int chunksize = array.size() / chunks;
        int count = -1;
        for (int i = 0; i < chunks; i++) {
            subLists = new ArrayList();
            int j = 0;
            while (j < chunksize && count < listSize) {
                subLists.add(array.get(++count));
                j++;
            }
            finalList.add(subLists);
        }
        // TODO: start recording time
        long startTime = System.currentTimeMillis();
        //Timer start:

        ExecutorService executor = Executors.newFixedThreadPool(chunks);
        for (int i = 0; i < finalList.size(); i++) {
            ArrayList<Integer> workinglist = (ArrayList<Integer>) finalList.get(i);
            Runnable run = new MyRunnable(workinglist);
            executor.execute(run);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("\nFinished all threads\n");
        computeMean(doublemeanlist);
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime+" milliseconds has been elapsed");
    }

    private static void computeMean(ArrayList<Double> array) {
        double sum = 0;
        double mean;
        for(double d : array){
            sum+=d;
        }
        mean = sum/array.size();
        System.out.println("this is the mean:");
        System.out.println(mean);

    }
    static class MyRunnable implements Runnable {
        private ArrayList<Integer> list;
        private double mean;
        public static ArrayList<Double> doublemeanlist = new ArrayList<Double>();
        public double computeMean(ArrayList<Integer> array){
            double sum = 0;
            for(int d : array){
                sum+=d;
            }
            return sum/array.size();
        }
        MyRunnable(ArrayList<Integer> list) {
            this.list = list;
        }
        @Override
        public void run() {
            try {
                mean = computeMean(list);
            } catch (Exception e) {
                System.out.println("Computation Error");
            }
            doublemeanlist.add(mean);
        }
    }
}

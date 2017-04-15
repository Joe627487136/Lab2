package Lab2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Lab2.MedianThread.MyRunnable.meadianlist;

public class MedianThread {
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
        System.out.println("Hey Nigga!");
        System.out.println("This is your median number bruh:");
        System.out.println(median(merge(meadianlist)));
        long estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println(estimatedTime+" milliseconds has been elapsed");
    }
    static class MyRunnable implements Runnable {
        private ArrayList<Integer> list;
        private ArrayList<Integer> sortedlist;
        public static ArrayList<ArrayList> meadianlist = new ArrayList<ArrayList>();
        MyRunnable(ArrayList<Integer> list) {
            this.list = list;
        }
        @Override
        public void run() {
            try {
                Collections.sort(list);
                sortedlist = list;
            } catch (Exception e) {
                System.out.println("Computation Error");
            }
            meadianlist.add(sortedlist);
        }
    }
    public static ArrayList<Integer> merge(ArrayList<ArrayList> l) {
        ArrayList<Integer> combinedList = new ArrayList<Integer>();
        for (ArrayList<Integer> i : l){
            combinedList.addAll(i);
        }
        Collections.sort(combinedList);
        return combinedList;
    }
    public static double median(ArrayList<Integer> m) {
        int middle = m.size()/2;
        if (m.size()%2 == 1) {
            return m.get(middle);
        } else {
            return (m.get(middle-1) + m.get(middle)) / 2.0;
        }
    }
}

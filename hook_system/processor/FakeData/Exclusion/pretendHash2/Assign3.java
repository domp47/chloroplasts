package Assignment3;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Dom's Computer on 2016-10-30.
 */
public class Assign3 {
    public static void main(String[] args) throws IOException {
        new Assign3();
    }

    private int swaps = 0;//count for number of swaps made

    public Assign3() throws IOException {
        File file = chooseFile("File to Read");// choose the file to read from

        if(file==null)//if no file is choosen end the program
            return;

        Scanner sc = new Scanner(new FileReader(file)); // create an instance of
                                                        // scanner to read the
                                                        // file

        int n, k;//n for number of elements to be inputed
                 //k for the number of elements in each heap

        //Part A
        try {//try to read an int
            n = Integer.parseInt(sc.next());
        } catch (java.lang.NumberFormatException e) {//if not end program
            System.out.println("File Format Not Recognized");
            return;
        }
        try {//try to read an int
            k = Integer.parseInt(sc.next());
        } catch (java.lang.NumberFormatException e) {//if not end program
            System.out.println("File Format Not Recognized");
            return;
        }

        int padding = Integer.toString(n).length();//padding for the number of leading zeros
        Node heaps, h;

        int nHeaps = n / k - 1;//get the number of heaps to be created

        heaps = new Node(createHeap(sc, padding, k), null);//create the first heap
        h = heaps;//point h to heap

        while (nHeaps-- > 0) {//create the list of heaps
            h.next = new Node(createHeap(sc, padding, k), null);
            h = h.next;
        }
        heaps = Radix(heaps, n);//sort the heaps using a k-headix sort

        PrintHeaps(heaps);//print the sorted heap

        sc.close();//close the scanner

        //Part B

        file = chooseFile("File to Store Generated Numbers");//spot to write the generated nums

        generateNums(file);//generate the numbers and write

        File fOut = chooseFile("File to Write output to");//spot to write output to

        if(file==null||fOut==null)//if either is null end program
            return;

        PrintWriter writer = new PrintWriter(fOut, "UTF-8");//writer to write to file

        n = 10000;
        k = 10000;

        for(k=k;k>=1;k/=10) {

            writer.println("N = "+n+", K = "+k);
            writer.println("INITIAL LIST");

            printlist(writer,n,file);//prints initial list

            writer.println("SORTED LIST");

            sc = new Scanner(new FileReader(file));//scanner to read
            long tStart = System.currentTimeMillis();//start time

            padding = Integer.toString(n).length();//padding

            nHeaps = n / k - 1;//number of heaps

            System.out.println("Sort for n="+n+" k="+k+":");


            heaps = new Node(createHeap(sc, padding, k), null);//create first heap
            h = heaps;//point h to heap

            while (nHeaps-- > 0) {//create the list of heaps
                h.next = new Node(createHeap(sc, padding, k), null);
                h = h.next;
            }

            System.out.println("Swaps during construction: "+swaps);
            swaps = 0;//set swaps to 0;

            heaps = Radix(heaps, n);//sort the heaps

            writeHeaps(heaps, writer);//write heaps to file

            System.out.println("Swaps during removal: "+swaps);
            swaps = 0;

            long tEnd = System.currentTimeMillis();//end time

            System.out.println("Total Execution Time: "+(tEnd-tStart)+" milliseconds.\n");
        }
        sc.close();//close scanner
        writer.close();//close writer
    }
    //creates a random sequence of numbers from 1-10,000
    private void generateNums(File f) throws UnsupportedEncodingException {
        int[] x = new int[10000];//array for sequence
        Start:
        for(int i = 0;i<x.length;i++){
            int v = (int)(Math.random()*x.length)+1;

            for(int j = 0;j<x.length;j++){//checks if the number is in the sequence already
                if(x[j]==v){//if it is return to outer loop and redo generation
                    i--;
                    continue Start;
                }
            }

            x[i] = v;//if its not in it add it

        }

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(f, "UTF-8");
        } catch (FileNotFoundException e) {//if no file then return
            return;
        }
        for(int i = 0;i<x.length;i++){//write every element in array
            writer.println(x[i]);
        }
        writer.close();
    }
    //prints initial list
    private void printlist(PrintWriter pw,int n,File file) throws FileNotFoundException {
        Scanner s = new Scanner(new FileReader(file));//scanner

        for(int i = 0;i<n;i++){//prints all the elements in the file
            String val = s.next();
            pw.println(val);
        }
    }
    //writes the heaps to file
    private void writeHeaps(Node Heaps, PrintWriter pw){
            while (Heaps.heap.length > 1) {
                pw.println(Integer.parseInt(trickleDown(Heaps)));//prints element and trickles down
            }
    }
    //prints the heap to console
    private void PrintHeaps(Node Heaps){
            while (Heaps.heap.length > 1) {
                System.out.println(Integer.parseInt(trickleDown(Heaps)));//prints and then trickles down
            }
    }
    //trickles down the heaps
    private String trickleDown(Node Heaps){
        if(Heaps==null)//if heap is null return null
            return null;
        String val = Heaps.heap[1];//value to be returned - smallest element in heap
        Heaps.heap[1] = trickleDown(Heaps.next);
        if(Heaps.next!=null&&Heaps.next.heap.length==1)//if the next heap is empty then set the node to null
            Heaps.next = null;


        //if the first element in the heap is null then fix the heap making it one smaller
        //else balance the heap to make sure it sasitfies the min-heap conditions
        Heaps.heap = (Heaps.heap[1]==null) ? fixHeap(Heaps.heap): BalHeap(Heaps.heap);

        return val;
    }
    //balances the heap
    private String[] BalHeap(String[] heap){
        String[] s = new String[heap.length];

        for(int i = 1;i<s.length;i++){//for the length of the heap
            s[i] = heap[i];

            int pos = i;//position for element
            int par = i/2;//parents position
            while(true){//while the item is less than its parent
                if(pos==1||Integer.parseInt(s[par])<Integer.parseInt(s[pos]))
                    break;
                String temp = s[pos];//swapps the parent and element
                s[pos] = s[par];
                s[par] = temp;
                swaps++;
                pos/=2;
                par = pos/2;
            }
        }
        return s;
    }
    //fixes the heap so it is one smaller and balanced
    private String[] fixHeap(String[] heap){
        String[] s = new String[heap.length-1];//new heap one smaller

        for(int i = 1;i<s.length;i++){
            s[i] = heap[i+1];

            int pos = i;
            int par = i/2;
            while(true){//while item is less than parent
                if(pos==1||Integer.parseInt(s[par])<Integer.parseInt(s[pos]))
                    break;
                String temp = s[pos];//swaps parent and element
                s[pos] = s[par];
                s[par] = temp;
                swaps++;
                pos/=2;
                par = pos/2;
            }
        }
        return s;
    }
    //radix sorts the list of heaps
    private Node Radix(Node heaps, int range) {
        Node h = heaps;
        Queue[] bucket;//queue for the buckets

        int column = h.heap.length - 1;//column to compare

        while (column >= 1) {//while theres a column to sort

            bucket = new Queue[range + 1];//bucket for every possibility

            while (h != null) {//for the length of the heaps
                int buct = Integer.parseInt(h.heap[column]);//bucket number

                if (bucket[buct] == null)//if the buckets not been initialized initialize it
                    bucket[buct] = new Queue();
                bucket[buct].enter(h.heap);//add element to that bucket

                h = h.next;//traverse to next heap
            }
            heaps = new Node(null, null);//next heaps to be sorted or returned
            h = heaps;
            for (int i = 0; i < bucket.length; i++) {
                while (bucket[i] != null && !bucket[i].empty()) {//for length of bucket add the contents to heap
                    h.next = (new Node(bucket[i].leave(), null));
                    h = h.next;
                }
            }
            heaps = heaps.next;
            h = heaps;
            column--;
        }

        return heaps;
    }

    //creates the heap for a given size
    private String[] createHeap(Scanner sc, int padding, int size) {
        String[] s = new String[size + 1];//heap size of size

        for (int i = 1; i < s.length; i++) {
            int x;//int to add to heap
            try{//if the scanner reads data then add it to the heap
                x = Integer.parseInt(sc.next());
            }catch(NoSuchElementException e) {//if it doesnt read anything it is EOF and stop adding
                break;
            }

            s[i] = String.format("%0" + padding + "d", x);//add the time to the string with the given padding

            int pos = i;
            int par = i / 2;
            while (true) {//while item is less than parent
                if (pos == 1 || Integer.parseInt(s[par]) < Integer.parseInt(s[pos]))
                    break;
                String temp = s[pos];
                s[pos] = s[par];
                s[par] = temp;
                swaps++;
                pos /= 2;
                par = pos / 2;
            }
        }
        return s;
    }


    private File chooseFile(String title) {// method to open up a file dialog and return the
        // file. returns null if no file is chosen
        File f = null;
        JFileChooser fc = new JFileChooser();// opens file dialog using swing
        fc.setDialogTitle(title);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");// filter to choose which files
        // to look for
        fc.setFileFilter(filter);
        int rVal = fc.showOpenDialog(null);// gets the button pressed
        if (rVal == JFileChooser.APPROVE_OPTION) {// if the button pressed is
            // the open button
            f = fc.getSelectedFile();// set the file to the file chosen
        }
        return f;
    }
}

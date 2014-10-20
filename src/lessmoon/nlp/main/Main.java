package lessmoon.nlp.main;

import lessmoon.nlp.adt.*;
import lessmoon.nlp.statistic.*;
import lessmoon.nlp.util.*;
import lessmoon.nlp.term.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Dictionary dic = new Dictionary();
        String dicname = "dic.txt";
        String trainset = "data.txt";
        for(int i = 0 ; i < args.length;i++){
            switch(args[i]){
            case "-d":
                if( ++i < args.length){
                    dicname = args[i];
                } else {
                    System.err.println("Option `-d' should be follow by dictionary file name.");
                }
                break;
            case "-t":
                if( ++i < args.length){
                    trainset = args[i];
                } else {
                    System.err.println("Option `-t' should be follow by trainging set file name.");
                }
                break;
            case "--help":
            case "-h":
                System.out.println("Usage Segment:");
                System.out.println("[-d dictionary name] set the dictionary file name,default is dic.txt");
                System.out.println("[-t training set file name]  set the train set filename,default is data.txt");
                System.out.println("[-h --help ] show this help ");
                return;
            default:
                System.err.println("Unknown option `" + args[i] + "' found.");
                return;
            }
        }
        
        final String d = dicname;
        final String ts = trainset;
        dic.LoadData(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader(new java.io.FileReader(d));
            public int getch() throws Exception {
                return bf.read();
            }
        });

        DataReader datareader = new DataReader(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader( new java.io.FileReader(ts));
            public int getch() throws Exception{
                return bf.read();
            }
        });

        TermTrieTree ttt = new TermTrieTree();
 
        DataNode n = null;
        while((n = datareader.readDataNode()) != null){
            ttt.insert(n.term,n.type);
        }
        TermStreamGraph tsg = null;
        TermWeightGetter twg = new TermWeightGetter(ttt,dic);
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while(scanner.hasNext()){
            String sentence = scanner.nextLine();
            if(!sentence.isEmpty())
                System.out.println((new TermStreamGraph(sentence,twg)).getBestPath());
        }
    }
}
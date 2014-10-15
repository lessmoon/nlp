package lessmoon.nlp.statistic;

import lessmoon.nlp.adt.*;

import java.io.*;

public class Main{
    public static void main(String[] args)throws Exception{
        DataReader datareader = new DataReader(new InputStream(){
            FileReader fin = new FileReader("data.txt");
            BufferedReader bf = new BufferedReader(fin);
            public int getch() throws Exception{
                return bf.read();
            }
        });
        TermTrieTree ttt = new TermTrieTree("");
        
        
        DataNode n = null;
        while((n = datareader.readDataNode()) != null){
            ttt.insert(n.term,0,n.type);
        }
        
        System.out.println(ttt);
        return;
    }
}
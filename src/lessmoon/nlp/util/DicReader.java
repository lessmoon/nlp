package lessmoon.nlp.util;

import lessmoon.nlp.statistic.*;

import java.util.*;


public class DicReader{
    Queue<DataNode> EntryBuffer = new LinkedList<DataNode>();
    InputStream is;
    char peek = ' ';
    int  p;
    
    DicReader(InputStream i){
        is = i;
    }

    char getch() throws Exception {
        p = is.getch();
        peek = (char)p;
        return peek;
    }

    void skipBlank() throws Exception {
        while( Character.isWhitespace(peek) )
            getch();
    }

    public DataNode readEntry() throws Exception {
        String term,type;
        if(!EntryBuffer.isEmpty())
            return EntryBuffer.poll();
        skipBlank();
        if(p != InputStream.STREAM_END_CHAR){
            term = getTerm();
            skipBlank();
            assert (peek == '\\'):"Term type should begin with `\\'";
            do{
                getch();
                type = getType();
                EntryBuffer.offer(new DataNode(term,type));
            } while(peek != '\\');

            getch();
        }

        return EntryBuffer.poll();
    }
    
    String getTerm() throws Exception {
        StringBuffer buf = new StringBuffer();
        while(!Character.isWhitespace(peek)){
            buf.append(peek);
            getch();
        }
        return buf.toString();
    }

    String getType() throws Exception {
        StringBuffer buf = new StringBuffer();

        while(peek != '/' && peek != '\\'){
            buf.append(peek);
            getch();
        }
        return buf.toString();
    }
    
    public static void main(String[] args)throws Exception{
        DicReader dicreader = new DicReader(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            public int getch()throws Exception{
                return bf.read();
            }
        });
        DataNode e = null;
        while((e = dicreader.readEntry()) != null){
            System.out.println(e);
        }
    }
}
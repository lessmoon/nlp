package lessmoon.nlp.statistic;

import java.io.*;
import java.util.*;

public class DataReader {
    InputStream is;
    int  p    = ' ';
    char peek = ' ';
    Queue<DataNode> CachedDataNode = new LinkedList<DataNode>();
    
    public DataReader(){
        is = new InputStream(){
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            public int getch() throws Exception {
                return bf.read();
            }
        };
    }

    public DataReader(InputStream i){
        is = i;
    }
    
    int getch() throws Exception {
        p = is.getch();
        peek = (char) p;
        return p;
    }
    
    
    
    void skipBlank() throws Exception {
        while( Character.isWhitespace(peek) )
            getch();
    }

    
    
    public DataNode readDataNode() throws Exception{
        DataNode n = CachedDataNode.poll();
        if(n != null) {
            return n;            
        } else {
            skipBlank();
            if(p == '['){//the terms group
                getch();//skip the '['
                StringBuffer buf = new StringBuffer();
                String term,type;
                do{
                    skipBlank();
                    term = readTerm();
                    getch();
                    type = readType();
                    CachedDataNode.offer(new DataNode(term,type));
                    skipBlank();
                    buf.append(term);
                } while(p != ']');
                getch();//skip ']'
                return new DataNode(buf.toString(),readType());
            }else if(p == InputStream.STREAM_END_CHAR)
                return null;
            else {
                /*Assuming the data is complete or nothing*/
                String term = readTerm();
                /*The peek is `/' now!*/
                getch();//We should abandon the `/'
                String type = readType();
                return new DataNode(term,type);
            }
        }
       
    }
    
    String readTerm() throws Exception {
        StringBuffer buf = new StringBuffer();
        while( peek != '/'){
            buf.append(peek);
            getch();
            //System.out.println(buf);
        }
        return buf.toString();
    }
    
    String readType() throws Exception {
        StringBuffer buf = new StringBuffer();
        while( !Character.isWhitespace(peek) && peek != ']' ){ // ']' is the end of a group terms           
            buf.append(peek);
            getch();
        }
        return buf.toString();
    }
    
    
    public static void main(String[] args) throws Exception {
        DataReader dr = new DataReader();
        while(true){
            System.out.println(dr.readDataNode());
        }
    }
}
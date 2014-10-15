package lessmoon.nlp.statistic;
import java.io.*;

public class DataReader {
    InputStream is;
    char peek = ' ';

    DataReader(){
        
        is = new InputStream(){
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            public char getch() throws Exception {
                return (char)bf.read();
            }
        };
    }

    DataReader(InputStream i){
        is = i;
    }
    
    char getch() throws Exception {
        peek = is.getch();
        return peek;
    }
    
    static boolean isBlankChar(final char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == InputStream.STREAM_END_CHAR;
    }
    
    void skipBlank() throws Exception {
        while( isBlankChar(peek) )
            getch();
    }

    public DataNode readDataNode() throws Exception{
        skipBlank();
        if(peek == InputStream.STREAM_END_CHAR)
            return null;
        /*Assuming the data is complete or nothing*/
        String term = readTerm();
        /*The peek is `/' now!*/
        getch();//We should abandon the `/'
        String type = readType();
        return new DataNode(term,type);
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
        while( !isBlankChar(peek) ){            
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
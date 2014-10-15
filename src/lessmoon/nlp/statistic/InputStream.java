package lessmoon.nlp.statistic;

public interface InputStream{
    public static final char  STREAM_END_CHAR = 0;
    public char getch() throws Exception;
    //public boolean hasNext() ;
}
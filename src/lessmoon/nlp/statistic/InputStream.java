package lessmoon.nlp.statistic;

public interface InputStream{
    public static final int  STREAM_END_CHAR = -1;
    public int getch() throws Exception;
    //public boolean hasNext() ;
}
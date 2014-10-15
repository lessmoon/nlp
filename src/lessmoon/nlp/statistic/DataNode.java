package lessmoon.nlp.statistic;

public class DataNode {
    public final String term;
    public final String type;
    
    public DataNode(final String t,final String ty){
        term = t;
        type = ty;
    }

    public String toString() {
        return term + ":" + type;
    }
    
    
}
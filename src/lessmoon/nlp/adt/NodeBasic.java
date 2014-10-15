package lessmoon.nlp.adt;

public abstract class NodeBasic {
    public final int tag;
    static public final int TERMTREE        = 0,
                            TERMINFONODE    = 1;

    public NodeBasic(final int t){
        tag = t;
    }

    public abstract NodeBasic insert(final String term,final int pos,final String type);
}
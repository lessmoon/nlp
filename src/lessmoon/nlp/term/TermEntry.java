package lessmoon.nlp.term;

import lessmoon.nlp.statistic.*;


public class TermEntry {
    public final DataNode   dn;
    public final int        weight;
    public TermEntry(final DataNode d,final int w){
        dn = d;
        weight = w;
    }
}
package lessmoon.nlp.term;

import lessmoon.nlp.adt.*;
import lessmoon.nlp.statistic.*;
import lessmoon.nlp.util.*;

import java.util.*;

public class TermWeightGetter {
    TermTrieTree tttree;
    lessmoon.nlp.util.Dictionary   dictionary;
    public static final int BASE_LEVEL        = 5;
    public static final int DICTIONARY_FACTOR = 100;
    public static final int ACCURACY_FACTOR   = 1000;
    public static final int ZERO_LEVEL        = 0;
    public static final int TERM_LENGTH_FACTOR= 1000;
    public TermWeightGetter(TermTrieTree ttt,lessmoon.nlp.util.Dictionary dic){
        tttree      = ttt;
        dictionary  = dic;
    }

    /*this function will change the datanode's type field*/
    public TermEntry getTermBestWeight(final String term){
        /*
         * TODO: Fetch weight from the training set and dictionary
         *       The final weight equals TSW + BASELINE
         *       Training Set Weight(TSW),Dictionary Weight(1 or 0)(DW) and BASELINE(1 for temp
         */
        String type = "n";
        TermInfoNode t = tttree.getTermInfo(term);
        int w = BASE_LEVEL;
        boolean flag = dictionary.hasTerm(term);
        if(t != null && !t.isEmpty()){
            int max = -1;
            for(Map.Entry<String,Integer>   e : t.getTermInfo()){
                if(max < e.getValue()){
                    max = e.getValue();
                    type = e.getKey();
                }
            }
            w += max ;
        } else if(! flag ){
            return new TermEntry(new DataNode(term,type),ZERO_LEVEL);
        }

        if(flag){
            for(String ty : dictionary.getTermInfoSet(term)){
                type = ty;
                break;
            }
            w += DICTIONARY_FACTOR;
        }

        w = (int)(Math.log(w) * (double)ACCURACY_FACTOR ) * (2 << (term.length() - 2)) ;
        return new TermEntry(new DataNode(term,type),w);
    }
}
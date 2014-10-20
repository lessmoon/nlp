package lessmoon.nlp.term;

import lessmoon.nlp.adt.*;
import lessmoon.nlp.statistic.*;
import lessmoon.nlp.util.*;

import java.util.*;

public class TermWeightGetter {
    TermTrieTree tttree;
    lessmoon.nlp.util.Dictionary   dictionary;
    public static final int ACCURACY_FACTOR   = 1000;
    //public static final int ZERO_LEVEL        = 1;
    public static int    MIN_LEVEL;
    public static double TRAINING_SET_FACTOR;
    public static double DICTIONARY_FACTOR;

    
    public TermWeightGetter(TermTrieTree ttt,lessmoon.nlp.util.Dictionary dic){
        tttree      = ttt;
        dictionary  = dic;
        TRAINING_SET_FACTOR = Math.log(tttree.size()) ;
        DICTIONARY_FACTOR = Math.log(dictionary.size());
        MIN_LEVEL         =  (int)((- TRAINING_SET_FACTOR - DICTIONARY_FACTOR ) *(double)ACCURACY_FACTOR) - 1;
    }

    public TermEntry getTermBestWeight(final String term){

        String type = "n";
        TermInfoNode t = tttree.getTermInfo(term);
        double w = 0;
        if(t != null && !t.isEmpty()){
            int max = -1;
            for(Map.Entry<String,Integer>   e : t.getTermInfo()){
                if(max < e.getValue()){
                    max = e.getValue();
                    type = e.getKey();
                }
            }
            w = Math.log(max) -  TRAINING_SET_FACTOR;
        } else if(dictionary.hasTerm(term)){
            for(String ty : dictionary.getTermInfoSet(term)){
                type = ty;
                break;
            }
            w = -DICTIONARY_FACTOR;
        } else {
            /*judge if it is number!*/
            return new TermEntry(new DataNode(term,type),MIN_LEVEL);
        }

        return new TermEntry(new DataNode(term,type),(int)(w * (double)ACCURACY_FACTOR));
    }
}
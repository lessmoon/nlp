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
    private double TRAINING_SET_FACTOR;
    private double DICTIONARY_FACTOR;
    private Set<Character> scnumbers = new HashSet<Character>();//simplified chinese number
    private Set<Character> acnumbers = new HashSet<Character>();//Arab chinese number

    public TermWeightGetter(TermTrieTree ttt,lessmoon.nlp.util.Dictionary dic){
        tttree      = ttt;
        dictionary  = dic;
        TRAINING_SET_FACTOR = Math.log(tttree.size()) ;
        DICTIONARY_FACTOR = Math.log(dictionary.size());
        MIN_LEVEL     =  (int)((- TRAINING_SET_FACTOR - DICTIONARY_FACTOR ) *(double)ACCURACY_FACTOR) - 1;
        Character[] dic1 = {'一','二','三','四','五','六','七','八','九','十','〇','十','百','千','万','亿'};
        Character[] dic2 = {'１','２','３','４','５','６','７','８','９','０'};

        for(Character e : dic1){
            scnumbers.add(e);
        }
        for(Character e : dic2){
            acnumbers.add(e);
        }

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
            w = MIN_LEVEL;
            if(term.length() > 0){
                if(scnumbers.contains(term.charAt(0))){                
                    numbermatch1:{
                        for(int i = 1;i < term.length();i++){
                            if(!scnumbers.contains(term.charAt(i))){
                                break numbermatch1;
                            }
                        }
                        w = -DICTIONARY_FACTOR * (double)ACCURACY_FACTOR;
                        type = "m";
                    }
                } else if(acnumbers.contains(term.charAt(0))) {
                    numbermatch2:{
                        for(int i = 1;i < term.length();i++){
                            if(!acnumbers.contains(term.charAt(i))){
                                break numbermatch2;
                            }
                        }
                        w = -DICTIONARY_FACTOR * (double)ACCURACY_FACTOR;
                        type = "m";
                    }
                }
            }
            return new TermEntry(new DataNode(term,type),(int)w);
        }

        return new TermEntry(new DataNode(term,type),(int)(w * (double)ACCURACY_FACTOR));
    }
}
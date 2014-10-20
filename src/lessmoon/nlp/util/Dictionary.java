package lessmoon.nlp.util;

import lessmoon.nlp.adt.*;
import lessmoon.nlp.statistic.*;

import java.util.*;


public class Dictionary {
    TermTrieTree ttt = new TermTrieTree();
    
    Dictionary(){
    }
    
    public void LoadData(InputStream is) throws Exception {
        DicReader dicreader = new DicReader(is);
        DataNode e = null;
        while((e = dicreader.readEntry()) != null){
            ttt.insert(e.term,e.type);
        }
    }
    
    /*
     * @return true if the term is in the dictionary
     */
    public boolean hasTerm(final String term) {
        TermTrieTree tmp = ttt;
        for(char c : term.toCharArray()){
            tmp = tmp.getTermTreeChildNode(c);
            if(tmp == null)
                return false;
        }
        return tmp.isValidTerm();/*for convenience,return true if the term is empty string*/
    }

    /* 
     * @return null if the term doesn't exist
     */
    public Set<String> getTermInfoSet(final String term){
        TermTrieTree tmp = ttt;
        for(char c : term.toCharArray()){
            tmp = tmp.getTermTreeChildNode(c);
            if(tmp == null)
                return null;
        }
        return tmp.getTermInfo().getTermTypes();
    }

    public int size(){
        return ttt.size();
    }
    
    public String toString(){
        return ttt.toString();
    }

    static public void main(String[] args) throws Exception {
        Dictionary dic = new Dictionary();
        dic.LoadData(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            public int getch() throws Exception {
                return bf.read();
            }
        });
        System.out.println(dic.getTermInfoSet("บร"));
    }
}
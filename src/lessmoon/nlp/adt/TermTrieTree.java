package lessmoon.nlp.adt;

import java.util.*;

abstract class NodeBasic {
    public final int tag;

    public NodeBasic(final int t){
        tag = t;
    }
    
    public abstract NodeBasic insert(final String term,final int pos,final String type);
    
    static public final int TERMTREE        = 0,
                            TERMINFONODE    = 1;
}

class TermInfoNode extends NodeBasic {
    Map<String,Integer> types = new HashMap<String,Integer>();
    
    public TermInfoNode(){
        super(TERMINFONODE);
    }

    public NodeBasic insert(final String term,final int pos,final String type) {
        Integer count = types.get(type);
        /*For Test*/
        System.out.println("OK");
        if( count == null ){
            types.put(type,1);
        } else {
            types.put(type,count.intValue() + 1);
        }
        return this;
    }
    
    public String toString() {
        StringBuffer buf  = new StringBuffer();
        buf.append("( ");
        Set< Map.Entry<String,Integer> >  s = types.entrySet();
        for( Map.Entry<String,Integer> e : s ){
            buf.append(e.getKey() + ":" + e.getValue() + " ");
        }
        buf.append(")");
        return buf.toString();
    }
}

public class TermTrieTree extends NodeBasic {
    Map<Integer,TermTrieTree> chars = new HashMap<Integer,TermTrieTree>();
    TermInfoNode tif =  new TermInfoNode();
    final String term;

    
    TermTrieTree(String t){
        super(TERMTREE);
        term = t;
    }

    public NodeBasic insert(final String t,final int pos,final String type){        
        NodeBasic n = null;
        if( pos >= t.length() ) {
            System.out.println("OK66");
            n = tif;
        } else {
            /*if the node doesn't exist,create it*/
            int c = t.charAt(pos);
            n  = chars.get(c);
            if(n == null){
                TermTrieTree tmp = new TermTrieTree(t.substring(0,pos + 1));
                chars.put((int)c,tmp);
                n = tmp;
            }
        }
        return n.insert(t,pos + 1,type);
    }
    
    public String getThisLevelTerm(){
        return term;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        int len = term.length();
        buf.append(term + "->" + tif.toString() + "\n");
        Set< Map.Entry<Integer,TermTrieTree> >  s = chars.entrySet();
        for( Map.Entry<Integer,TermTrieTree> e : s ){
            for(int i = 0 ; i < len;i++)
                buf.append(" ");
            buf.append("|" + (char)e.getKey().intValue() + ":"  + e.getValue());
        }
        System.out.println(term + ".count:" + s.size());
        return buf.toString();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TermTrieTree ttt = new TermTrieTree("");
        while(true){
            String term  = scanner.nextLine();
            String type  = scanner.nextLine();
            ttt.insert(term,0,type);
            System.out.print(ttt.toString());
        }
    }
}
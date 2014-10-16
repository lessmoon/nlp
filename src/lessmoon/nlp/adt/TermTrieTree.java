package lessmoon.nlp.adt;

import java.util.*;

public class TermTrieTree extends NodeBasic {
    Map<Integer,TermTrieTree> chars = new HashMap<Integer,TermTrieTree>();
    TermInfoNode tif =  new TermInfoNode();
    final String term;

    public TermTrieTree(){
        super(TERMTREE);
        term = "";
    }

    protected TermTrieTree(String t){
        super(TERMTREE);
        term = t;
    }

    public NodeBasic insert(final String t,final String type){
        return insert(t,0,type);
    }
    
    public NodeBasic insert(final String t,final int pos,final String type){        
        NodeBasic n = null;
        if( pos >= t.length() ) {
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
    
    public boolean isValidTerm(){
        return !tif.isEmpty();
    }
    
    public String getTerm(){
        return term;
    }
    
    public TermInfoNode getTermInfo(){
        return tif;
    }

    public TermTrieTree getTermTreeChildNode(final char c){
        return chars.get((int)c);
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
        return buf.toString();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TermTrieTree ttt = new TermTrieTree();
        while(true){
            String term  = scanner.nextLine();
            String type  = scanner.nextLine();
            ttt.insert(term,0,type);
            System.out.print(ttt.toString());
        }
    }
}
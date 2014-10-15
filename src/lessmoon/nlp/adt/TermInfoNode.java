package lessmoon.nlp.adt;

import java.util.*;

class TermInfoNode extends NodeBasic {
    Map<String,Integer> types = new HashMap<String,Integer>();
    
    public TermInfoNode(){
        super(TERMINFONODE);
    }

    public NodeBasic insert(final String term,final int pos,final String type) {
        Integer count = types.get(type);

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
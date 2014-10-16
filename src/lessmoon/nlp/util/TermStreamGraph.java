package lessmoon.nlp.util;

import java.util.*;

class TermGraphNode {
    TermGraphNode  nextnode;
    int            weight;
    DataNode       term;             
    boolean        isMainPathNode;


    public TermGraphNode(DataNode t,int w,TermGraphNode next){
        term = t;
        weight = w;
        nextnode = next;
        isMainPathNode = false;
    }

    public TermGraphNode(DataNode t,int w){
        term = t;
        weight = w;
        nextnode = null;
        isMainPathNode = false;
    }

    public TermGraphNode getNext(){
        return nextnode;
    }

    public ArrayList<TermGraphNode> getNextNodeList(){
        ArrayList<TermGraphNode> res = new ArrayList<TermGraphNode>();
        res.add(nextnode);
        return res;
    }
    
    public void setNext(TermGraphNode next){
        nextnode = next;
    }

    public boolean hasNext(){
        return nextnode != null;
    }
}

class MainPathNode extends TermGraphNode{
    ArrayList<TermGraphNode> nextlist = new ArrayList<TermGraphNode>();
    
    public MainPathNode(DataNode t,int w,TermGraphNode next){
        super(t,w,next);
        isMainPathNode = true;
    }

    public MainPathNode(DataNode t,int w){
        super(t,w);
        isMainPathNode = true;
    }

    public void addNextNode(TermGraphNode next){
        nextlist.add(next);
    }

    public ArrayList<TermGraphNode> getNextNodeList(){
        return nextlist;
    }
}

public class TermStreamGraph {
    ArrayList< MainPathNode > naturalterms;
    MainPathNode  end,start;

    public TermStreamGraph(final String sentence){
        naturalterms = new ArrayList< MainPathNode >(sentence.length() + 2);

        int weight = 1;
        naturalterms.add(start = new MainPathNode(null,1));

        for(char c : sentence.getCharArray()){
            /*
             * TODO: Fetch weight from the training set and dictionary
             *       The final weight equals TSW + DW * CONSTANT + BASELINE
             *       Training Set Weight(TSW),Dictionary Weight(1 or 0)(DW) and BASELINE(1 for temp)
             */
            DataNode tmp = null;
            
            /*TODO:Choose the single-letter-term info with MAX weight*/
            
            naturalterms.add(new TermGraphNode(tmp,weight));
        }
        naturalterms.add(end  = new MainPathNode(null,1,null));

        for(int i = 0 ; i < naturalterms.size() - 1;i++){
            naturalterms.get(i).setNext(i + 1);
        }
        //start.addNextNode(end);
    }
    
    public void addTerm(final int pos,DataNode d,final int weight){
        TermGraphNode t = new TermGraphNode(d,weight);
        naturalterms.at(pos).addNextNode(t);
        t.setNext(naturalterms.at(d.term.length() + pos + 1));
    }

    public TermGraphNode getBestPath(){
        
    }
}
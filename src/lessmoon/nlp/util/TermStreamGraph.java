package lessmoon.nlp.util;

import java.util.*;

class TermGraphNode {
    final int         weight;
    TermGraphNode     nextnode;
    final DataNode    term;             
    boolean           isMainPathNode;


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

    public int getWeight(){
        return weight;
    }
    
    public ArrayList<TermGraphNode> getNextNodeList(){
        ArrayList<TermGraphNode> res = new ArrayList<TermGraphNode>();
        res.add(nextnode);
        return res;
    }
    
    public void setNext(TermGraphNode next){
        nextnode = next;
    }

    public DataNode getTerm(){
        return term;
    }
    
    public boolean hasNext(){
        return nextnode != null;
    }
    
    public String toString(){
        return term.toString() + nextnode.toString();
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

class MaxPathGenerator {
    Map<PathNode,Integer> pimap = new HashMap<PathNode,Integer>();
    ArrayList<PathNode> ipmap = new ArrayList<PathNode>();
    int[] pweights;
    
    MaxPathGenerator(){
        
    }
    
    int countNumber(PathNode s,PathNode d){
        parseGraph(s,p);/*Give each node a number to identify them*/
        return pimap.size();
    }

    void parseGraph(PathNode s,PathNode d){
        pimap.put(s,pimap.size() - 1);
        ipmap.add(s);
        if(s == d){
            return;
        } else {
            for(TermGraphNode e : s.getNextNodeList()){
                if(pimap.get(e) == null){ 
                    parseGraph(e,d);
                }
            }
        }
    }
    
    public TermGraphNode maxPath(PathNode s,PathNode d){
        pweights = new int[countnumber(s,d)];
        for(int i = 0 ; i < pweights.size();i++){
            pweights[i] = -1;
        }
        int max = -1;
        TermGraphNode mid = null;
        for(TermGraphNode e : s.getNextNodeList()){
            TermGraphNode tmp = getPath();
            if(max < pweights[pimap.get(e).intValue()]){
                mid = e;
            }
        }
        return new TermGraphNode(ipmap[idx_s].getTerm(),s.getWeight(),mid);
    }

    TermGraphNode getPath(PathNode s,PathNode d){
        if(s == d){
            pweights[pimap.get(d).intValue()] = d.getWeight();
            return d;
        }
        int max = -1;
        int idx_s = pimap.get(s).intValue();
        TermGraphNode mid = null;
        for(TermGraphNode e : s.getNextNodeList()){
            int idx = pimap.get(e).intValue();
            int tmp = pweights[idx];
            if(tmp < 0){/*It is not a valid path weight value*/
                ipmap[idx] = getPath(e,d);
            }
            if(max < tmp){
                max = tmp;
                mid = ipmap[idx];
            }
        }
        pweights[idx_s] = max + s.getWeight();
        return new TermGraphNode(ipmap[idx_s].getTerm(),s.getWeight(),mid);
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
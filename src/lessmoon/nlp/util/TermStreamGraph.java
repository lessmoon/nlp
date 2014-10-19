package lessmoon.nlp.util;

import lessmoon.nlp.adt.*;
import lessmoon.nlp.statistic.*;
import lessmoon.nlp.term.*;

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
        if(nextnode != null){
            res.add(nextnode);
        }
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
        return "" + term + "->" + nextnode;
    }
}

class MainPathNode {
    ArrayList<TermGraphNode> nextlist = new ArrayList<TermGraphNode>();
    //ArrayList<TermGraphNode> fullnextList = new ArrayList<TermGraphNode>();
    
    public MainPathNode(DataNode t,int w,TermGraphNode next){
        super(t,w,next);
        addNextNode(next);
        isMainPathNode = true;
    }

    public MainPathNode(DataNode t,int w){
        super(t,w);
        isMainPathNode = true;
    }
    
    public void setNext(TermGraphNode next){
        if(nextnode == null){
            super.setNext(nextnode);
            addNextNode(nextnode);
        }
    }
    
    public void addNextNode(TermGraphNode next){
        nextlist.add(next);
    }

    public ArrayList<TermGraphNode> getNextNodeList(){
        return nextlist;
    }
}

class MaxPathGenerator {
    Map<TermGraphNode,Integer> pimap = new HashMap<TermGraphNode,Integer>();
    ArrayList<TermGraphNode> ipmap = new ArrayList<TermGraphNode>();
    int[] pweights;
    
    MaxPathGenerator(){
        
    }
    
    int countNumber(TermGraphNode s,TermGraphNode d){
        parseGraph(s,d);/*Give each node a number to identify them*/
        return pimap.size();
    }

    void parseGraph(TermGraphNode s,TermGraphNode d){
        if( s == null ){
            return ;
        }
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
    
    public TermGraphNode maxPath(TermGraphNode s,TermGraphNode d){
        pweights = new int[countNumber(s,d)];
        for(int i = 0 ; i < pweights.length;i++){
            pweights[i] = -1;
        }
        int max = -1;
        TermGraphNode mid = null;
        for(TermGraphNode e : s.getNextNodeList()){
            TermGraphNode tmp = getPath(e,d);
            if(max < pweights[pimap.get(e).intValue()]){
                mid = e;
            }
        }
        return new TermGraphNode(s.getTerm(),s.getWeight(),mid);
    }

    TermGraphNode getPath(TermGraphNode s,TermGraphNode d){
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
                ipmap.set(idx,getPath(e,d));
            }
            if(max < tmp){
                max = tmp;
                mid = ipmap.get(idx);
            }
        }
        pweights[idx_s] = max + s.getWeight();
        return new TermGraphNode(s.getTerm(),s.getWeight(),mid);
    }
}

public class TermStreamGraph {
    ArrayList< MainPathNode > naturalterms;
    MainPathNode  end,start;

    public TermStreamGraph(final String sentence,TermWeightGetter twg){
        naturalterms = new ArrayList< MainPathNode >();

        int weight = 1;
        naturalterms.add(start = new MainPathNode(null,1));

        for(char c : sentence.toCharArray()){
            
            /*TODO:Choose the single-letter-term info with MAX weight*/
            TermEntry e = twg.getTermBestWeight(String.valueOf(c));
            naturalterms.add(new MainPathNode(e.dn,weight));
        }
        naturalterms.add(end  = new MainPathNode(null,1,null));

        for(int i = 0 ; i < naturalterms.size() - 1;i++){
            naturalterms.get(i).setNext(naturalterms.get( i + 1));
        }

        /*
         * Insert every possible term
         */
        
        for(int p = 0; p < sentence.length() - 1;p++){
            for( int i = 2; i <= sentence.length() - p ; i++){
                String subterm = sentence.substring(p,p + i);
                TermEntry e = twg.getTermBestWeight(subterm);
                if(e.weight > TermWeightGetter.ZERO_LEVEL) {/*If it is valid term*/
                    /*Possible term*/
                    System.out.println("Possible term:" + e.dn);
                    addTerm(p,e.dn,e.weight);
                }
            }
        }
        //start.addNextNode(end);
    }
    
    private void addTerm(final int pos,DataNode d,final int weight){
        /*Wow  dd*/
        TermGraphNode t = new TermGraphNode(d,weight);
        naturalterms.get(pos).addNextNode(t);
        t.setNext(naturalterms.get(d.term.length() + pos + 1));
    }

    public TermGraphNode getBestPath(){
        return (new MaxPathGenerator()).maxPath(start,end);
    }
    
    public static void main(String[] args) throws Exception{
        Dictionary dic = new Dictionary();
        dic.LoadData(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader(new java.io.FileReader("dic.txt"));
            public int getch() throws Exception {
                return bf.read();
            }
        });

        DataReader datareader = new DataReader(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader( new java.io.FileReader("data.txt"));
            public int getch() throws Exception{
                return bf.read();
            }
        });

        TermTrieTree ttt = new TermTrieTree();
 
        DataNode n = null;
        while((n = datareader.readDataNode()) != null){
            ttt.insert(n.term,n.type);
        }
        TermStreamGraph tsg = null;
        Scanner scanner = new Scanner(System.in);
        TermWeightGetter twg = new TermWeightGetter(ttt,dic);
        while(true){
            System.out.println((new TermStreamGraph(scanner.nextLine(),twg)).getBestPath());
        }
    }
}